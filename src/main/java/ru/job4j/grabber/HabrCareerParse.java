package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);

    public static void main(String[] args) throws IOException {
        HabrCareerParse parse = new HabrCareerParse();
        for (int i = 1; i <= 1; i++) {
            Connection connection = Jsoup.connect(String.format("%s%s%d", PAGE_LINK, "?page=", i));
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                Element dateElement = row.select(".vacancy-card__date").first();
                String lt = dateElement.child(0).attr("datetime");
                System.out.printf("%s %s %s%n", vacancyName, lt, link);
                try {
                    System.out.println(parse.retrieveDescription(link));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private String retrieveDescription(String link)  throws IOException {
        return Jsoup.connect(link).get()
                .select(".vacancy-description__text").get(0)
                .text();
    }
}
