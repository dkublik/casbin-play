package pl.dk.casbinplay.dsl;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.apache.commons.collections.CollectionUtils;
import pl.dk.casbinplay.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HasRoleFunction extends AbstractFunction {

    private final static String ROLES_SEPARATOR = ";";
    private final static String LIST_START_MARKER = "[";
    private final static String STRING_MARKER = "'";

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject subjectArg, AviatorObject siteArg, AviatorObject rolesArg) {
        Subject subject = (Subject) FunctionUtils.getJavaObject(subjectArg, env);
        String site = FunctionUtils.getStringValue(siteArg, env);
        String rolesString = FunctionUtils.getStringValue(rolesArg, env);
        List<String> rolesListed = toList(rolesString);
        List<String> subjectRoles = subject.getRoles(site);
        boolean anyRoleMatches = CollectionUtils.containsAny(subjectRoles, rolesListed);
        return AviatorBoolean.valueOf(anyRoleMatches);
    }

    private List<String> toList(String stringList) {
        if (stringList.startsWith(LIST_START_MARKER)) { // get rid of "[]"
            stringList = stringList.substring(1, stringList.length() - 1);
        }
        List<String> list = new ArrayList<>();
        for (String role: stringList.split(ROLES_SEPARATOR)) {
            role = sanitize(role);
            list.add(role);
        }
        return list;
    }

    private String sanitize(String string) {
        string = string.trim();
        if (string.startsWith(STRING_MARKER)) { // get rid of "'"
            string = string.substring(1, string.length() - 1);
        }
        return string;
    }

    @Override
    public String getName() {
        return "hasRole";
    }
}
