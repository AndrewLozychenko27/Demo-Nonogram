package ua.lozychenko.nonogram.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("game")
public class GameProperty {
    private final Hint hint = new Hint(
            1,
            0.1);

    public Hint getHint() {
        return hint;
    }

    public static class Hint {
        private Integer min;
        private Double threshold;

        public Hint(Integer min, Double threshold) {
            this.min = min;
            this.threshold = threshold;
        }

        public Integer getMin() {
            return min;
        }

        public void setMin(Integer min) {
            this.min = min;
        }

        public Double getThreshold() {
            return threshold;
        }

        public void setThreshold(Double threshold) {
            this.threshold = threshold;
        }
    }
}