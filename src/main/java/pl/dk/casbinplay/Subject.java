package pl.dk.casbinplay;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class Subject {
    String login;
    Map<String, List<String>> site2Roles;
}
