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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.forms.business.Form;
import fr.paris.lutece.plugins.forms.business.FormHome;
import fr.paris.lutece.plugins.forms.business.FormResponse;
import fr.paris.lutece.plugins.forms.business.FormResponseHome;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Provider creation of FormsProviderManager: a form without mapping is a configuration error that makes the action fail (rolled back),
 * a vanished response or a missing history only gives no provider (no NullPointerException).
 */
public class FormsProviderManagerTest extends LuteceTestCase
{
    private Form _form;
    private FormResponse _formResponse;

    @AfterEach
    public void cleanUp( )
    {
        if ( _formResponse != null )
        {
            FormResponseHome.remove( _formResponse.getId( ) );
        }
        if ( _form != null )
        {
            FormHome.remove( _form.getId( ) );
        }
    }

    /**
     * No mapping for the form of the response: the action fails with an explicit AppException (rolled back, the response keeps its state),
     * not a silent success without notification.
     */
    @Test
    public void testActionFailsWhenTheFormHasNoMapping( )
    {
        _form = new Form( );
        _form.setTitle( "FormsProviderManagerTest" );
        _form.setBreadcrumbName( "forms.horizontalBreadcrumb" );
        FormHome.create( _form );

        _formResponse = new FormResponse( );
        _formResponse.setFormId( _form.getId( ) );
        _formResponse.setGuid( "guid" );
        _formResponse.setSteps( new ArrayList<>( ) );
        FormResponseHome.create( _formResponse );

        AppException exception = assertThrows( AppException.class,
                ( ) -> new FormsProviderManager( ).createProvider( String.valueOf( _form.getId( ) ), history( _formResponse.getId( ) ), null ) );
        assertTrue( exception.getMessage( ).contains( "No mapping found for the form " + _form.getId( ) ), exception.getMessage( ) );
    }

    /**
     * A form response that no longer exists: no provider, no NullPointerException.
     */
    @Test
    public void testNoProviderForAnUnknownFormResponse( )
    {
        assertNull( new FormsProviderManager( ).createProvider( "1", history( 999999 ), null ) );
    }

    /**
     * No resource history: no provider.
     */
    @Test
    public void testNoProviderWithoutResourceHistory( )
    {
        assertNull( new FormsProviderManager( ).createProvider( "1", null, null ) );
    }

    private static ResourceHistory history( int nIdResource )
    {
        ResourceHistory resourceHistory = new ResourceHistory( );
        resourceHistory.setIdResource( nIdResource );
        resourceHistory.setResourceType( FormResponse.RESOURCE_TYPE );
        return resourceHistory;
    }
}
