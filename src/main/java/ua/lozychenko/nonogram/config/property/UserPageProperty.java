package ua.lozychenko.nonogram.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("page.user")
public record UserPageProperty(Integer defaultSize, Integer[] sizeRange) {
}