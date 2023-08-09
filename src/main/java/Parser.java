import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private static Document getPage() throws IOException {
        String url = "https://pogoda.mail.ru/prognoz/moskva/14dney/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    private static ArrayList<String> dates(Document page) {
        Elements dates = page.select("div [class=hdr__wrapper]");
        ArrayList<String> list = new ArrayList<>();
        for (Element element : dates) {
            list.add(element.text());
        }
        list.remove(0);
        return list;
    }

    private static ArrayList<String> weather(Document page) {
        ArrayList<String> weather = new ArrayList<>();
        Elements info = page.select("div [class=p-flex__column p-flex__column_percent-16]");
        for (Element element : info) {
            Elements elements = element.select("span");
            StringBuilder res = new StringBuilder();
            for (Element e : elements) {
                if (e.text().isEmpty()) continue;
                res.append(e.text()).append(" ");
            }
            weather.add(result(res.toString()));
        }
        return weather;
    }

    private static String result(String s) {
        String[] tmp = s.split(" ");
        StringBuilder result = new StringBuilder();
        result.append("\n" + tmp[0].toUpperCase() + "\n");
        result.append("\n");
        result.append("Температура: " + tmp[1]);
        result.append("\n");
        result.append("Погода: " + tmp[2]);
        result.append("\n");
        result.append(tmp[3] + " " + tmp[4] + " " + tmp[5]);
        result.append("\n");
        result.append("Давление: " + tmp[6] + " " + tmp[7] + " " + tmp[8] + " " + tmp[9]);
        result.append("\n");
        result.append("Влажность: " + tmp[14]);
        result.append("\n");
        result.append("Ветер: " + tmp[16] + " " + tmp[17] + " " + tmp[18]);
        result.append("\n");
        result.append("uv: (интенсивоность солнечных лучей): " + tmp[22]);
        result.append("\n");
        result.append("Вероятность дождя :" + tmp[24]);
        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        Document page = getPage();
        ArrayList<String> dates = dates(page);
        ArrayList<String> weather = weather(page);
        Map<String, ArrayList<String>> dates_weather = new HashMap<>();
        for (int i = 0; i < dates.size(); i++) {
            String key = (dates.get(i).toUpperCase());
            dates_weather.put(key, new ArrayList<>());
            for (int j = i * 4; j < (i + 1) * 4; j++) {
                dates_weather.get(key).add(weather.get(j));
            }
        }
        for (Map.Entry<String, ArrayList<String>> entry : dates_weather.entrySet()) {
            System.out.println(entry.getKey());
            for (String s : entry.getValue()) {
                System.out.println(s);
            }
        }
    }
}
