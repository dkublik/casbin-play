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
        String obj = "/data1"
        String act = "read"

        expect:
        enforcer.enforce(alice, obj, act) == true
    }

}
