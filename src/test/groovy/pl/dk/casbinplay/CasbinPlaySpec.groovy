package pl.dk.casbinplay

import org.casbin.jcasbin.main.Enforcer
import spock.lang.Specification

class CasbinPlaySpec extends Specification {

    def "should work"() {
        given:
        String modelPath = this.class.classLoader.getResource('security/model.conf').path
        String policyPath = this.class.classLoader.getResource('security/policy.csv').path
        Enforcer enforcer = new Enforcer(modelPath, policyPath)
        Subject alice = new Subject('Alice', 21)
        Resource article = new Resource('content', 'Standard:Article')
        String action = 'read'

        expect:
        enforcer.enforce(alice, article, action) == true
    }

}
