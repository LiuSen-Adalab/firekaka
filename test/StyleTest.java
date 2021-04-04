import css.CSSParser;
import css.Stylesheet;
import dom.Node;
import html.HTMLParser;
import style.StyledNode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class StyleTest {
    public static void main(String[] args) throws IOException {
        String htmlInput = Files.readString(Path.of("res/style-html-input.html"), StandardCharsets.UTF_8);
        String cssInput = Files.readString(Path.of("res/style-css-input.css"), StandardCharsets.UTF_8);
        String expectedOutput = Files.readString(Path.of("res/style-output.xml"), StandardCharsets.UTF_8);
//        String htmlInput = Files.readString(Path.of("testFile/style-html-input.html"), StandardCharsets.UTF_8);
//        String cssInput = Files.readString(Path.of("testFile/style-css-input.css"), StandardCharsets.UTF_8);
//        String expectedOutput = Files.readString(Path.of("testFile/style-output.xml"), StandardCharsets.UTF_8);

        HTMLParser htmlParser = new HTMLParser();
        Node domNode = htmlParser.parse(htmlInput);

        CSSParser CSSParser = new CSSParser();
        Stylesheet stylesheet = CSSParser.parse(cssInput);

        StyledNode root = new StyledNode(domNode, stylesheet);
        String output = root.toString();

        System.out.println(output);
//        assert output.equals(expectedOutput);

    }
}
