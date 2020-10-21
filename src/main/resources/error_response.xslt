<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:param name="STATUS" />	
	<xsl:param name="ERRORDESC" />
	
	<xsl:template match="/">
	<RESPONSE_DETAILS>
            <STATUS><xsl:value-of select="$STATUS" /></STATUS>           
           <!--  </xsl:if> -->
           <xsl:if test="$STATUS='Failure'">
            <ERRORDETAILS>
               <xsl:for-each select="$ERRORDESC">               
               <ERRORDESC><xsl:value-of select="." /></ERRORDESC>
               </xsl:for-each>
            </ERRORDETAILS>
            </xsl:if>
         </RESPONSE_DETAILS>
     </xsl:template>
   </xsl:stylesheet>
	