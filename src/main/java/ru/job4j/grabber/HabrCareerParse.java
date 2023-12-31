package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);

    private static final int NUMBER_OF_PAGES_TO_BE_PARSED = 1;

    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private String retrieveDescription(String descrLink) throws IOException {
        return Jsoup.connect(descrLink).get()
                .select(".vacancy-description__text").get(0)
                .text();
    }

    private Post getPostFromHabrElement(Element row) throws IOException {
        Post post = null;
        Element titleElement = row.select(".vacancy-card__title").first();
        Element linkElement = titleElement.child(0);
        String vacancyName = titleElement.text();
        String sourceLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
        Element dateElement = row.select(".vacancy-card__date").first();
        String dateTime = dateElement.child(0).attr("datetime");
        try {
            post = new Post(vacancyName, sourceLink,
                    retrieveDescription(sourceLink), dateTimeParser.parse(dateTime));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return post;
    }

    @Override
    public List<Post> list(String pageLink) throws IOException {
        List<Post> result = new ArrayList<>();
        for (int i = 1; i <= NUMBER_OF_PAGES_TO_BE_PARSED; i++) {
            Connection connection = Jsoup.connect(String.format("%s%s%d", pageLink, "?page=", i));
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                try {
                    result.add(getPostFromHabrElement(row));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return result;
    }

    public List<Post> getListExternalUse() throws IOException {
        return this.list(PAGE_LINK);
    }

    public static void main(String[] args) throws IOException {
        HabrCareerParse parse = new HabrCareerParse(new HabrCareerDateTimeParser());
        List<Post> example = parse.list(PAGE_LINK);
        example.forEach(post -> {
            System.out.println(post.toString());
            System.out.println(post.getDescription());
        });
    }
}
