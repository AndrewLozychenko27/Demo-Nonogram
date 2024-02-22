package ua.lozychenko.nonogram.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("pages")
public class PagesProperty {
    private final Size puzzle = new Size(
            11,
            new Integer[]{11, 23, 47, 95});
    private final Size user = new Size(
            10,
            new Integer[]{10, 25, 50, 100});

    public Size getPuzzle() {
        return puzzle;
    }

    public Size getUser() {
        return user;
    }

    public static class Size {
        private Integer defaultSize;
        private Integer[] sizeRange;

        public Size(Integer defaultSize, Integer[] sizeRange) {
            this.defaultSize = defaultSize;
            this.sizeRange = sizeRange;
        }

        public Integer getDefaultSize() {
            return defaultSize;
        }

        public void setDefaultSize(Integer defaultSize) {
            this.defaultSize = defaultSize;
        }

        public Integer[] getSizeRange() {
            return sizeRange;
        }

        public void setSizeRange(Integer[] sizeRange) {
            this.sizeRange = sizeRange;
        }
    }
}