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

import fr.paris.lutece.plugins.forms.business.Form;
import fr.paris.lutece.plugins.forms.business.FormHome;
import fr.paris.lutece.plugins.forms.business.Question;
import fr.paris.lutece.plugins.forms.business.QuestionHome;
import fr.paris.lutece.plugins.forms.service.provider.GenericFormsProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.modulenotifygrumappingmanager.service.AbstractProviderManagerWithMapping;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.action.ActionService;
import fr.paris.lutece.plugins.workflowcore.service.provider.IProvider;
import fr.paris.lutece.plugins.workflowcore.service.provider.InfoMarker;
import fr.paris.lutece.plugins.workflowcore.service.provider.ProviderDescription;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.util.ReferenceList;

/**
 * <p>
 * This class represents a provider manager for {@link Directory} objects
 * </p>
 * <p>
 * One provider per {@code Directory} object
 * </p>
 *
 */
public class FormsProviderManager extends AbstractProviderManagerWithMapping
{
    @Inject
    private ActionService _actionService;

    /**
     * Constructor
     * 
     * @param strId
     *            the id of this provider manager
     */
    public FormsProviderManager( String strId )
    {
        super( strId );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Only the providers for directories linked to the current workflow are returned
     * </p>
     */
    @Override
    public Collection<ProviderDescription> getAllProviderDescriptions( ITask task )
    {
        Collection<ProviderDescription> collectionProviderDescriptions = new ArrayList<>( );

        int nIdWorkflow = _actionService.findByPrimaryKey( task.getAction( ).getId( ) ).getWorkflow( ).getId( );
        
        for ( Form form : FormHome.getFormList( ) )
        {
            if ( ( nIdWorkflow == form.getIdWorkflow( ) ) )
            {
                ProviderDescription providerDescription = new ProviderDescription( String.valueOf( form.getId( ) ),
                        I18nService.getLocalizedString( Constants.TITLE_I18NKEY, I18nService.getDefaultLocale( ) ) + form.getTitle( ) );
                collectionProviderDescriptions.add( providerDescription );
            }
        }
        collectionProviderDescriptions.add( new ProviderDescription( Constants.ALL_FORMS + nIdWorkflow ,
                I18nService.getLocalizedString( Constants.TITLE_I18NKEY, I18nService.getDefaultLocale( ) ) + Constants.DESCRIPTION_ALLFORMS_SUFFIX ) );
        
        return collectionProviderDescriptions;
    }
 
    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderDescription getProviderDescription( String strProviderId )
    {
        Collection<InfoMarker> collectionNotifyMarkers = new ArrayList<>( );
        ProviderDescription providerDescription;
        
    	if(strProviderId.startsWith( Constants.ALL_FORMS ) ) 
    	{
    		
    		int nIdWorkflow= Integer.parseInt( strProviderId.substring(1));
    		   providerDescription = new ProviderDescription( strProviderId,
                      I18nService.getLocalizedString( Constants.TITLE_I18NKEY, I18nService.getDefaultLocale( ) ) + Constants.DESCRIPTION_ALLFORMS_SUFFIX );
    		 collectionNotifyMarkers.addAll(getProviderMarkerDescriptions( FormHome.getFormList( ).stream().filter(form -> form.getIdWorkflow() == nIdWorkflow).collect(Collectors.toList()) ));
    		
    	}else {
    		
    		Form form = FormHome.findByPrimaryKey( Integer.parseInt( strProviderId ) );

             providerDescription = new ProviderDescription( String.valueOf( form.getId( ) ),
                    I18nService.getLocalizedString( Constants.TITLE_I18NKEY, I18nService.getDefaultLocale( ) ) + form.getTitle( ) );

             collectionNotifyMarkers= GenericFormsProvider.getProviderMarkerDescriptions( form );
        
    	}
    	
    	providerDescription.setMarkerDescriptions( collectionNotifyMarkers );
        return providerDescription;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IProvider createProvider( String strProviderId, ResourceHistory resourceHistory, HttpServletRequest request )
    {
        return new FormsProvider( getId( ), strProviderId, resourceHistory, request );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ProviderDescription> getAllProviderDescriptions( )
    {
        Collection<ProviderDescription> collectionProviderDescriptions = new ArrayList<>( );
        List<Form> listForms = FormHome.getFormList( );

        for ( Form form : listForms )
        {
            ProviderDescription providerDescription = new ProviderDescription( String.valueOf( form.getId( ) ),
                    I18nService.getLocalizedString( Constants.TITLE_I18NKEY, I18nService.getDefaultLocale( ) ) + form.getTitle( ) );
            collectionProviderDescriptions.add( providerDescription );
        }

        return collectionProviderDescriptions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getMappingPropertiesForProvider( String strProviderId )
    {
         return QuestionHome.getQuestionsReferenceListByForm( Integer.parseInt( strProviderId ) );
    }
    
    /**
     * Get the collection of InfoMarker, for the given form
     * 
     * @param listForm
     *            The form list
     * @return the collection of the notifyMarkers
     */
    private Collection<InfoMarker> getProviderMarkerDescriptions( List<Form> listForm )
    {
        Collection<InfoMarker> collectionNotifyMarkers = new ArrayList<>( );
        List<Question> questionList = new ArrayList<>( );
        
        for(Form form: listForm) {
        	
        	questionList.addAll( QuestionHome.getListQuestionByIdForm( form.getId( ) ));
        }

        for ( Question formQuestion : questionList )
        {
        	if( collectionNotifyMarkers.stream().noneMatch( p -> p.getMarker( ).equals(formQuestion.getCode( )))) {
        		
        		InfoMarker notifyMarker = new InfoMarker( formQuestion.getCode( ));
        		notifyMarker.setDescription( formQuestion.getColumnTitle( ) );
        		collectionNotifyMarkers.add( notifyMarker );
        	}
        }
        InfoMarker notifyMarkerURl = new InfoMarker( Constants.MARK_URL_ADMIN_RESPONSE );
        notifyMarkerURl.setDescription( I18nService.getLocalizedString( Constants.MESSAGE_DESCRIPTION, I18nService.getDefaultLocale( ) ) );
        InfoMarker creationDateMarker = new InfoMarker( Constants.MARK_CREATION_DATE );
        creationDateMarker.setDescription( I18nService.getLocalizedString( Constants.MESSAGE_CREATION_DATE, I18nService.getDefaultLocale( ) ) );
        InfoMarker creationTimeMarker = new InfoMarker( Constants.MARK_CREATION_TIME );
        creationTimeMarker.setDescription( I18nService.getLocalizedString( Constants.MESSAGE_CREATION_TIME, I18nService.getDefaultLocale( ) ) );
        InfoMarker updateDateMarker = new InfoMarker( Constants.MARK_UPDATE_DATE );
        updateDateMarker.setDescription( I18nService.getLocalizedString( Constants.MESSAGE_UPDATE_DATE, I18nService.getDefaultLocale( ) ) );
        InfoMarker updateTimeMarker = new InfoMarker( Constants.MARK_UPDATE_TIME );
        updateTimeMarker.setDescription( I18nService.getLocalizedString( Constants.MESSAGE_UPDATE_TIME, I18nService.getDefaultLocale( ) ) );
        collectionNotifyMarkers.add( notifyMarkerURl );
        collectionNotifyMarkers.add( creationDateMarker );
        collectionNotifyMarkers.add( creationTimeMarker );
        collectionNotifyMarkers.add( updateDateMarker );
        collectionNotifyMarkers.add( updateTimeMarker );
        InfoMarker urlQrCodeBo = new InfoMarker( Constants.MARK_QRCODE_URL_BO );
        urlQrCodeBo.setDescription( I18nService.getLocalizedString( Constants.MESSAGE_URL_QR_CODE_BO, I18nService.getDefaultLocale( ) ) );
        InfoMarker urlQrCodeFo = new InfoMarker( Constants.MARK_QRCODE_URL_FO );
        urlQrCodeFo.setDescription( I18nService.getLocalizedString( Constants.MESSAGE_URL_QR_CODE_FO, I18nService.getDefaultLocale( ) ) );
        collectionNotifyMarkers.add(urlQrCodeBo);
        collectionNotifyMarkers.add(urlQrCodeFo);
        return collectionNotifyMarkers;
    }
    

}
