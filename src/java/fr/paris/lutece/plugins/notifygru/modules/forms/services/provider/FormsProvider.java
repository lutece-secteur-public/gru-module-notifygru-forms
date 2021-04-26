/*
 * Copyright (c) 2002-2021, City of Paris
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

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.forms.business.FormResponse;
import fr.paris.lutece.plugins.forms.business.QuestionHome;
import fr.paris.lutece.plugins.forms.service.provider.GenericFormsProvider;
import fr.paris.lutece.plugins.modulenotifygrumappingmanager.business.NotifygruMappingManager;
import fr.paris.lutece.plugins.modulenotifygrumappingmanager.business.NotifygruMappingManagerHome;
import fr.paris.lutece.plugins.notifygru.modules.forms.services.INotifyGruFormsService;
import fr.paris.lutece.plugins.notifygru.modules.forms.services.NotifyGruFormsService;
import fr.paris.lutece.plugins.workflow.service.provider.ProviderManagerUtil;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

/**
 * This class represents a provider for a {@link Forms} object
 *
 */
public class FormsProvider extends GenericFormsProvider
{
    // PROPERTY KEY
    private static final String PROPERTY_SMS_SENDER_NAME = "workflow-notifygruforms.gruprovider.sms.sendername";

    // PARAMETERS
    public static final String PARAMETER_VIEW_FORM_RESPONSE_DETAILS = "view_form_response_details";
    public static final String PARAMETER_ID_FORM_RESPONSES = "id_form_response";

    // SERVICES
    private static INotifyGruFormsService _notifyGruFormsService = SpringContextService.getBean( NotifyGruFormsService.BEAN_SERVICE );

    // FIELDS
    private final String _strCustomerEmail;
    private final String _strConnectionId;
    private final String _strCustomerId;
    private final String _strCustomerPhoneNumber;
    private final String _strDemandReference;
    private final String _strDemandTypeId;
    private final NotifygruMappingManager _mapping;

    /**
     * Constructor
     * 
     * @param strProviderManagerId
     *            the provider manager id. Used to retrieve the mapping.
     * @param strProviderId
     *            the provider id. Corresponds to the {@code Forms} id. Used to retrieve the mapping.
     * @param resourceHistory
     *            the resource history. Corresponds to the {@link FormResponse} object containing the data to provide
     * @param request
     *            the request
     */
    public FormsProvider( String strProviderManagerId, String strProviderId, ResourceHistory resourceHistory, HttpServletRequest request )
    {
        super( resourceHistory, request );

        // Load the mapping manager
        _mapping = NotifygruMappingManagerHome.findByPrimaryKey( ProviderManagerUtil.buildCompleteProviderId( strProviderManagerId, strProviderId ) );

        if ( _mapping == null )
        {
            throw new AppException(
                    "No mapping found for the form " + _formResponse.getFormId( ) + ". Please check the configuration of the module-forms-mappingmanager." );
        }

        _strCustomerEmail = _notifyGruFormsService.getEmail( _mapping, _formResponse );
        _strConnectionId = _notifyGruFormsService.getConnectionId( _mapping, _formResponse );
        _strCustomerId = _notifyGruFormsService.getCustomerId( _mapping, _formResponse );
        _strCustomerPhoneNumber = _notifyGruFormsService.getSMSPhoneNumber( _mapping, _formResponse );
        _strDemandReference = _notifyGruFormsService.getDemandReference( _mapping, _formResponse );
        _strDemandTypeId = String.valueOf( _notifyGruFormsService.getIdDemandType( _mapping ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideDemandTypeId( )
    {
        return _strDemandTypeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideDemandSubtypeId( )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideDemandReference( )
    {
        return _strDemandReference;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideCustomerConnectionId( )
    {
        return _strConnectionId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideCustomerId( )
    {
        return _strCustomerId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideCustomerEmail( )
    {
        return _strCustomerEmail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideSmsSender( )
    {
        return AppPropertiesService.getProperty( PROPERTY_SMS_SENDER_NAME );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideCustomerMobilePhone( )
    {
        return _strCustomerPhoneNumber;
    }

    /**
     * Ghe the reference list of the questions / positions for the given provider id
     * 
     * @param strProviderId
     *            The provider id
     * @return the reference list of the questions / positions.
     */
    public static ReferenceList getQuestionPositions( String strProviderId )
    {
        return QuestionHome.getQuestionsReferenceListByForm( Integer.parseInt( strProviderId ) );
    }

}
