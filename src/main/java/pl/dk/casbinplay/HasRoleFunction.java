package pl.dk.casbinplay;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HasRoleFunction extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg) {
        String rolesString = FunctionUtils.getStringValue(arg, env);
        List<String> rolesListed = toList(rolesString);
        String currentSite = (String) env.get("r_site");
        Subject subject = (Subject) env.get("r_subject");
        List<String> subjectRolesForCurrentSite = subject.getSite2Roles().get(currentSite);
        boolean anyRoleMatches = CollectionUtils.containsAny(subjectRolesForCurrentSite, rolesListed);
        return AviatorBoolean.valueOf(anyRoleMatches);
    }

    private List<String> toList(String stringList) {
        if (stringList.startsWith("[")) { // get rid of "[]"
            stringList = stringList.substring(1, stringList.length() - 1);
        }
        List<String> list = new ArrayList<>();
        for (String role: stringList.split(";")) {
            role = sanitize(role);
            list.add(role);
        }
        return list;
    }

    private String sanitize(String string) {
        string = string.trim();
        if (string.startsWith("\"")) { // get rid of "[]"
            string = string.substring(1, string.length() - 1);
        }
        return string;
    }

    @Override
    public String getName() {
        return "hasRole";
    }
}
