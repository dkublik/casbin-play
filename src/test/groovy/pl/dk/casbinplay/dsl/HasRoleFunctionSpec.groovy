package pl.dk.casbinplay.dsl

import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType
import com.googlecode.aviator.runtime.type.AviatorString
import pl.dk.casbinplay.Subject
import spock.lang.Specification
import spock.lang.Unroll

class HasRoleFunctionSpec extends Specification {

    HasRoleFunction hasRoleFunction = new HasRoleFunction()

    @Unroll
    def "should evaluate any role script"() {
        given:
        pl.dk.casbinplay.Subject subject = new Subject('Alice', subjectRoles)

        when:
        Boolean result = hasRoleFunction.call([:], AviatorRuntimeJavaType.valueOf(subject), new AviatorString(currentSite), new AviatorString(anyRole)).getValue([:])

        then:
        result == expectedResult

        where:
        subjectRoles                                        |   currentSite             |   anyRole                     || expectedResult
        ['paramountplus-italy': ['Producer']]               |   'paramountplus-italy'   |  "['Producer'; 'Admin';]"     || true
        ['paramountplus-italy': ['Producer']]               |   'paramountplus-italy'   |  "['Producer']"               || true
        ['paramountplus-italy': ['Producer', 'Admin']]      |   'paramountplus-italy'   |  "['Producer']"               || true
        ['paramountplus-italy': ['Producer']]               |   'testq-nick'            |  "['Producer']"               || false
        [:]                                                 |   'paramountplus-italy'   |  "['Producer']"               || false
        ['paramountplus-italy': ['Producer']]               |   'paramountplus-italy'   |  ""                           || false
    }
}
