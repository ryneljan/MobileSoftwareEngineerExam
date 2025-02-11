package com.raineru.msee

import org.junit.Assert
import org.junit.Test

class FullNameValidatorTest {

    @Test
    fun testValidFullNames() {
        Assert.assertTrue("John Doe".isValidFullName())
        Assert.assertTrue("Doe, John".isValidFullName())
        Assert.assertTrue("Jane Doe".isValidFullName())
        Assert.assertTrue("John Smith III".isValidFullName())
        Assert.assertTrue("Dr. Jane Doe".isValidFullName())
        Assert.assertTrue("Doe, John.".isValidFullName())
        Assert.assertTrue("Doe. John,".isValidFullName())
        Assert.assertTrue("Doe, John".isValidFullName())
        Assert.assertTrue("Doe John,".isValidFullName())
    }

    @Test
    fun testInvalidFullNames() {
        Assert.assertFalse("".isValidFullName())
        Assert.assertFalse("123".isValidFullName())
        Assert.assertFalse("John Doe123".isValidFullName())
        Assert.assertFalse("John Doe!".isValidFullName())
        Assert.assertFalse("John Doe#".isValidFullName())
        Assert.assertFalse("John Doe$".isValidFullName())
        Assert.assertFalse("John Doe%".isValidFullName())
        Assert.assertFalse("John Doe^".isValidFullName())
        Assert.assertFalse("John Doe&".isValidFullName())
        Assert.assertFalse("John Doe*".isValidFullName())
        Assert.assertFalse("John Doe(".isValidFullName())
        Assert.assertFalse("John Doe)".isValidFullName())
        Assert.assertFalse("John Doe-".isValidFullName())
        Assert.assertFalse("John Doe_".isValidFullName())
        Assert.assertFalse("John Doe+".isValidFullName())
        Assert.assertFalse("John Doe=".isValidFullName())
        Assert.assertFalse("John Doe?".isValidFullName())
        Assert.assertFalse("John Doe/".isValidFullName())
        Assert.assertFalse("John Doe\\".isValidFullName())
        Assert.assertFalse("John Doe|".isValidFullName())
        Assert.assertFalse("John Doe;".isValidFullName())
        Assert.assertFalse("John Doe:".isValidFullName())
        Assert.assertFalse("John Doe<".isValidFullName())
        Assert.assertFalse("John Doe>".isValidFullName())
        Assert.assertFalse("John Doe[".isValidFullName())
        Assert.assertFalse("John Doe]".isValidFullName())
        Assert.assertFalse("John Doe{".isValidFullName())
        Assert.assertFalse("John Doe}".isValidFullName())
        Assert.assertFalse("John Doe`".isValidFullName())
        Assert.assertFalse("John Doe~".isValidFullName())
    }
}