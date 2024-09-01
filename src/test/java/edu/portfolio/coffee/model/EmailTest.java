package edu.portfolio.coffee.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class EmailTest {

    @Test
    public void testEmail() {
        assertThrows(IllegalArgumentException.class, () -> {Object email = new Email(null);} );
    }

    @Test
    public void testEmailOK() {
        var email = new Email("email@example.com");
        assertTrue(email.getAddress().equals("email@example.com"));
    }

    @Test
    public void testEmailSame() {
var email = new Email("email@example.com");
var email2 = new Email("email@example.com");
assertEquals(email, email2);
    }
}