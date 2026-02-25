package br.com.poc.tests;

import com.intuit.karate.junit5.Karate;

class PocTestsRunner {
    
    @Karate.Test
    Karate runAll() {
        return Karate.run("classpath:features").relativeTo(getClass());
    }    

}
