package pl.dk.casbinplay

import org.casbin.jcasbin.main.Enforcer
import spock.lang.Specification

class CasbinPlaySpec extends Specification {

    def "should work"() {
        given:
        Enforcer enforcer = enforcer()
        Map<String, List<String>> sites2Roles = [
                'paramountplus-italy': ['Producer'],
                'testq-nick': ['INTL Shared Producer', 'Shared Producer']
        ]
        Subject alice = new Subject('Alice', sites2Roles)
        Resource article = new Resource('content', 'Standard:Article')
        String action = 'read'
        String site = 'paramountplus-italy'

        expect:
        enforcer.enforce(alice, article, site, action) == true
    }

   private Enforcer enforcer() {
       String modelPath = this.class.classLoader.getResource('security/model.conf').path
       String policyPath = this.class.classLoader.getResource('security/policy.csv').path
       Enforcer enforcer = new Enforcer(modelPath, policyPath)
       HasRoleFunction hasRoleFunction = new HasRoleFunction()
       enforcer.addFunction(hasRoleFunction.name, hasRoleFunction)
       return enforcer
   }
}
