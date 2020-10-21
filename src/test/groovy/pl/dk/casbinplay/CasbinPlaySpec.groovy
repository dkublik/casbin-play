package pl.dk.casbinplay

import org.casbin.jcasbin.main.Enforcer
import pl.dk.casbinplay.dsl.EvalGroovyFunction
import pl.dk.casbinplay.dsl.HasRoleFunction
import spock.lang.Specification
import spock.lang.Unroll

class CasbinPlaySpec extends Specification {

    Enforcer enforcer = enforcer()

    @Unroll
    def "eny role should view menu (#role, #action)"() {
        given:
        Map<String, List<String>> sites2Roles = [
                'paramountplus-italy': [role],
        ]
        Subject alice = new Subject('Alice', sites2Roles)
        MenuResource menu = new MenuResource()
        String site = 'paramountplus-italy'

        expect:
        enforcer.enforce(alice, menu, site, action) == isAllowed

        where:
        role                                                      |   action   || isAllowed
        'Producer'                                                |   'view'   || true
        'Admin'                                                   |   'view'   || true
        'INTL Shared Producers'                                   |   'view'   || true
        'Producer - No Series | No Season | No Authority Types'   |   'view'   || true
    }

    @Unroll
    def "Producer permissions (#contentType, #action)"() {
        given:
        Map<String, List<String>> sites2Roles = [
                'paramountplus-italy': ['Producer'],
        ]
        Subject alice = new Subject('Alice', sites2Roles)
        ContentResource content = new ContentResource(contentType)
        String site = 'paramountplus-italy'

        expect:
        enforcer.enforce(alice, content, site, action) == isAllowed

        where:
        contentType         |   action      || isAllowed
        'Standard:Article'  |   'Create'    || true
        'Standard:Series'   |   'Edit'      || true
    }

    @Unroll
    def "'Producer - No Series | No Season | No Authority Types' permissions (#contentType, #action)"() {
        given:
        Map<String, List<String>> sites2Roles = [
                'paramountplus-italy': ['Producer - No Series | No Season | No Authority Types'],
        ]
        Subject alice = new Subject('Alice', sites2Roles)
        ContentResource content = new ContentResource(contentType)
        String site = 'paramountplus-italy'

        expect:
        enforcer.enforce(alice, content, site, action) == isAllowed

        where:
        contentType         |   action      || isAllowed
        'Standard:Article'  |   'Delete'    || true
        'Standard:Series'   |   'Delete'    || false
        'Standard:Series'   |   'Publish'   || true
    }

    @Unroll
    def "'INTL Shared Producer' field permissions (#fieldName, #action)"() {
        given:
        Map<String, List<String>> sites2Roles = [
                'paramountplus-italy': ['INTL Shared Producer'],
        ]
        Subject alice = new Subject('Alice', sites2Roles)
        FieldResource field = new FieldResource(fieldName)
        String site = 'paramountplus-italy'

        expect:
        enforcer.enforce(alice, field, site, action) == isAllowed

        where:
        fieldName           |   action      || isAllowed
        'Title'             |   'Edit'      || true
        'DistPolicies'      |   'Edit'      || false
    }

   private Enforcer enforcer() {
       String modelPath = this.class.classLoader.getResource('security/model.conf').path
       String policyPath = this.class.classLoader.getResource('security/policy.csv').path
       Enforcer enforcer = new Enforcer(modelPath, policyPath)
       List.of(new HasRoleFunction(), new EvalGroovyFunction()).forEach(it -> {
           enforcer.addFunction(it.getName(), it)
       })
       return enforcer
   }
}
