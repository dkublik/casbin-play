package pl.dk.casbinplay;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ContentResource {
    private final String type = "content";
    private final String contentType;
}
