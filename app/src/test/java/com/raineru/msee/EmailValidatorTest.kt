package com.raineru.msee

import org.junit.Assert
import org.junit.Test

class EmailValidatorTest {

    @Test
    fun testValidEmails() {
        Assert.assertTrue("test@example.com".isValidEmail())
        Assert.assertTrue("john.doe@example.co.uk".isValidEmail())
        Assert.assertTrue("jane_doe123@subdomain.example.com".isValidEmail())
        Assert.assertTrue("user+tag@example.com".isValidEmail())
        Assert.assertTrue("12345@example.com".isValidEmail())
        Assert.assertTrue("test@example-domain.com".isValidEmail())
        Assert.assertTrue("test@example.museum".isValidEmail())
        Assert.assertTrue("test@example.travel".isValidEmail())
    }

    @Test
    fun testInvalidEmails() {
        Assert.assertFalse("".isValidEmail())
        Assert.assertFalse("test".isValidEmail())
        Assert.assertFalse("test@".isValidEmail())
        Assert.assertFalse("@example.com".isValidEmail())
        Assert.assertFalse("test@.com".isValidEmail())
        Assert.assertFalse("test@example.".isValidEmail())
        Assert.assertFalse("test@@example.com".isValidEmail())
        Assert.assertFalse("test@example..com".isValidEmail())
        Assert.assertFalse("test..user@example.com".isValidEmail())
        Assert.assertFalse(".test@example.com".isValidEmail())
        Assert.assertFalse("test.@example.com".isValidEmail())
        Assert.assertFalse("test@example@com".isValidEmail())
        Assert.assertFalse("test@example..com".isValidEmail())
        Assert.assertFalse("test@.com".isValidEmail())
        Assert.assertFalse("test@example.".isValidEmail())
        Assert.assertFalse("test@example.c".isValidEmail())
        Assert.assertFalse("test@example.123".isValidEmail())
        Assert.assertFalse("test@example.com-".isValidEmail())
        Assert.assertFalse("test@example.com_".isValidEmail())
        Assert.assertFalse("test@example.com+".isValidEmail())
        Assert.assertFalse("test@example.com=".isValidEmail())
        Assert.assertFalse("test@example.com?".isValidEmail())
        Assert.assertFalse("test@example.com/".isValidEmail())
        Assert.assertFalse("test@example.com\\".isValidEmail())
        Assert.assertFalse("test@example.com|".isValidEmail())
        Assert.assertFalse("test@example.com;".isValidEmail())
        Assert.assertFalse("test@example.com:".isValidEmail())
        Assert.assertFalse("test@example.com<".isValidEmail())
        Assert.assertFalse("test@example.com>".isValidEmail())
        Assert.assertFalse("test@example.com[".isValidEmail())
        Assert.assertFalse("test@example.com]".isValidEmail())
        Assert.assertFalse("test@example.com{".isValidEmail())
        Assert.assertFalse("test@example.com}".isValidEmail())
        Assert.assertFalse("test@example.com`".isValidEmail())
        Assert.assertFalse("test@example.com~".isValidEmail())
    }
}