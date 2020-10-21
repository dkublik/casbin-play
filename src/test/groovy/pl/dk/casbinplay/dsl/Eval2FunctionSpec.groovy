package pl.dk.casbinplay.dsl

import com.googlecode.aviator.runtime.type.AviatorString
import pl.dk.casbinplay.ContentResource
import spock.lang.Specification
import spock.lang.Subject

class Eval2FunctionSpec extends Specification {

    @Subject
    EvalGroovyFunction eval2Function = new EvalGroovyFunction()

    def "should evaluate script"() {
        given:
        ContentResource resource = new ContentResource('Standard:Season')
        Map<String, Object> env = ['r_resource': resource]
        String script = "r_resource.contentType in ['Standard:Season'; 'Standard:Series']"

        when:
        Boolean result = eval2Function.call(env, new AviatorString(script)).getValue(env)

        then:
        result
    }
}
