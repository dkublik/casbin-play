package pl.dk.casbinplay;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FieldResource {
    private final String type = "field";
    private final String name;
}
