/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.notifygru.modules.forms.services;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.forms.business.FormQuestionResponse;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponseHome;
import fr.paris.lutece.plugins.forms.business.FormResponse;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.modulenotifygrumappingmanager.business.NotifygruMappingManager;

/**
 * NotifyFormsService.
 */
public final class NotifyGruFormsService implements INotifyGruFormsService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = "notifygru-forms.notifygruFormsService";

    /**
     * Instantiates a new notify gru directory service.
     */
    private NotifyGruFormsService( )
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormQuestionResponse> getListFormQuestionResponse( FormResponse formResponse )
    {
        return FormQuestionResponseHome.getFormQuestionResponseListByFormResponse( formResponse.getId( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmail( NotifygruMappingManager mapping, FormResponse formResponse )
    {
        return getFormResponseStringValue( mapping.getEmail( ), formResponse );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIdDemand( FormResponse formResponse )
    {
        return formResponse.getId( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIdDemandType( NotifygruMappingManager mapping )
    {
        return mapping.getDemandeTypeId( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSMSPhoneNumber( NotifygruMappingManager mapping, FormResponse formResponse )
    {
        return getFormResponseStringValue( mapping.getMobilePhoneNumber( ), formResponse );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConnectionId( NotifygruMappingManager mapping, FormResponse formResponse )
    {
        return getFormResponseStringValue( mapping.getConnectionId( ), formResponse );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCustomerId( NotifygruMappingManager mapping, FormResponse formResponse )
    {
        return getFormResponseStringValue( mapping.getCustomerId( ), formResponse );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDemandReference( NotifygruMappingManager mapping, FormResponse formResponse )
    {
        return getFormResponseStringValue( mapping.getDemandReference( ), formResponse );
    }

    /**
     * Get the form response string value with given nIdResponse and given formResponse
     * 
     * @param nIdResponse
     *            The id response
     * @param formResponse
     *            The form response
     * @return the form response String value
     */
    private String getFormResponseStringValue( int nIdResponse, FormResponse formResponse )
    {
    	List<Response> responseList = FormQuestionResponseHome.getFormQuestionResponseListByFormResponse( formResponse.getId( ) ).stream( )
                .filter( response -> response.getQuestion( ).getId( ) == nIdResponse ).findFirst( )
                .map( FormQuestionResponse::getEntryResponse ).orElse( null );
    	
    	if ( CollectionUtils.isNotEmpty( responseList ) )
    	{
    		return responseList.get( 0 ).getToStringValueResponse( );
    	}
    	return StringUtils.EMPTY;
    }
}
