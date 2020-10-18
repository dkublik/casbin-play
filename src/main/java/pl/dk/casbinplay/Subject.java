package pl.dk.casbinplay;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@AllArgsConstructor
@Data
public class Subject {
    private final String login;
    private final Map<String, List<String>> site2Roles;

    public List<String> getRoles(String site) {
        return site2Roles.getOrDefault(site, emptyList());
    }
}
