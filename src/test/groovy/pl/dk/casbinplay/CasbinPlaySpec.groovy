package pl.dk.casbinplay

import org.casbin.jcasbin.main.Enforcer
import pl.dk.casbinplay.dsl.Eval2Function
import pl.dk.casbinplay.dsl.HasRoleFunction
import spock.lang.Specification
import spock.lang.Unroll

class CasbinPlaySpec extends Specification {

    Enforcer enforcer = enforcer()

    @Unroll
    def "Producer permissions (#contentType, #action)"() {
        given:
        Map<String, List<String>> sites2Roles = [
                'paramountplus-italy': ['Producer'],
        ]
        Subject alice = new Subject('Alice', sites2Roles)
        Resource article = new Resource('content', contentType)
        String site = 'paramountplus-italy'

        expect:
        enforcer.enforce(alice, article, site, action) == isAllowed

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
        Resource article = new Resource('content', contentType)
        String site = 'paramountplus-italy'

        expect:
        enforcer.enforce(alice, article, site, action) == isAllowed

        where:
        contentType         |   action      || isAllowed
        'Standard:Article'  |   'Delete'    || true
        'Standard:Series'   |   'Delete'    || false
        'Standard:Series'   |   'Publish'   || true
    }

   private Enforcer enforcer() {
       String modelPath = this.class.classLoader.getResource('security/model.conf').path
       String policyPath = this.class.classLoader.getResource('security/policy.csv').path
       Enforcer enforcer = new Enforcer(modelPath, policyPath)
       List.of(new HasRoleFunction(), new Eval2Function()).forEach(it -> {
           enforcer.addFunction(it.getName(), it)
       })
       return enforcer
   }
}
