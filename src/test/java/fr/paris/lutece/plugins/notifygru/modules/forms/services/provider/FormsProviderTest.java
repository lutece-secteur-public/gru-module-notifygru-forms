/*
 * Copyright (c) 2002-2026, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.notifygru.modules.forms.services.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.workflowcore.service.provider.InfoMarker;

/**
 * The values of the question markers (LUT-33153): HTML built from the escaped responses of the user, the responses to a
 * repeated question separated by <code>&lt;br&gt;</code>; a rich text response is kept as HTML and sanitized by the
 * notification.
 */
public class FormsProviderTest
{
    private static final String HOSTILE_RESPONSE = "Dupont & fils <b>x</b> \"q\" 'a' <script>alert(1)</script>";
    private static final String HOSTILE_RESPONSE_HTML = "Dupont &amp; fils &lt;b&gt;x&lt;/b&gt; &quot;q&quot; 'a' &lt;script&gt;alert(1)&lt;/script&gt;";

    /**
     * A hostile response is written as text: no tag and no attribute come from it.
     */
    @Test
    public void testHostileResponseIsEscaped( )
    {
        assertEquals( HOSTILE_RESPONSE_HTML, FormsProvider.responseToHtml( HOSTILE_RESPONSE, false ) );
    }

    /**
     * A rich text response stays HTML: notifygru sanitizes it when it renders the message (rich provider markers).
     */
    @Test
    public void testRichTextResponseIsKept( )
    {
        String strRichText = "<p>Bonjour <strong>agent</strong></p><script>alert(1)</script><img src=x onerror=alert(2)>";
        assertEquals( strRichText, FormsProvider.responseToHtml( strRichText, true ) );
    }

    /**
     * The title and URL of a file response are escaped.
     */
    @Test
    public void testFileLineIsEscaped( )
    {
        assertEquals( "photo &lt;img src=x onerror=alert(1)&gt;.png: http://localhost:8080/lutece/file?id=1&amp;key=a&quot;b\n",
                FormsProvider.fileToHtml( "photo <img src=x onerror=alert(1)>.png", "http://localhost:8080/lutece/file?id=1&key=a\"b" ) );
    }

    /**
     * The responses to a repeated question are joined with a line break, each one escaped.
     */
    @Test
    public void testRepeatedResponsesAreJoinedWithLineBreak( )
    {
        InfoMarker marker = new InfoMarker( "position_12" );
        FormsProvider.setValue( marker, FormsProvider.responseToHtml( "first <i>one</i>", false ) );
        assertEquals( "first &lt;i&gt;one&lt;/i&gt;", marker.getValue( ) );

        FormsProvider.setValue( marker, FormsProvider.responseToHtml( HOSTILE_RESPONSE, false ) );
        assertEquals( "first &lt;i&gt;one&lt;/i&gt;<br>" + HOSTILE_RESPONSE_HTML, marker.getValue( ) );
    }
}
