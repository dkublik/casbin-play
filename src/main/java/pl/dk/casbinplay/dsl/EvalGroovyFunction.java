package pl.dk.casbinplay.dsl;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.util.Map;

public class EvalGroovyFunction extends AbstractFunction {

    private final static char SCRIPT_LIST_SEPARATOR = ';'; // need to be ';' cause ',' is used to separate policy arguments
    private final static char GROOVY_LIST_SEPARATOR = ',';

    private final CompilerConfiguration config;

    public EvalGroovyFunction() {
        config = new CompilerConfiguration();
        config.setScriptBaseClass(PoliciesScript.class.getCanonicalName());
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg) {
        String script = FunctionUtils.getStringValue(arg, env);
        script = script.replace(SCRIPT_LIST_SEPARATOR, GROOVY_LIST_SEPARATOR);
        Binding binding = new Binding();
        env.entrySet().forEach(entry ->
            binding.setProperty(entry.getKey(), entry.getValue())
        );
        GroovyShell shell = new GroovyShell(EvalGroovyFunction.class.getClassLoader(), binding, config);
        Boolean result = (Boolean) shell.evaluate(script);
        return AviatorBoolean.valueOf(result);
    }

    @Override
    public String getName() {
        return "eval";
    }
}
