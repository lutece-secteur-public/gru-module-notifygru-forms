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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.forms.business.Form;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponse;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponseHome;
import fr.paris.lutece.plugins.forms.business.FormResponse;
import fr.paris.lutece.plugins.forms.business.FormResponseHome;
import fr.paris.lutece.plugins.forms.service.entrytype.EntryTypeComment;
import fr.paris.lutece.plugins.forms.util.FormsConstants;
import fr.paris.lutece.plugins.forms.web.admin.MultiviewFormResponseDetailsJspBean;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.Field;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.EntryTypeServiceManager;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.IEntryTypeService;
import fr.paris.lutece.plugins.modulenotifygrumappingmanager.business.NotifygruMappingManager;
import fr.paris.lutece.plugins.modulenotifygrumappingmanager.business.NotifygruMappingManagerHome;
import fr.paris.lutece.plugins.workflow.service.provider.ProviderManagerUtil;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.provider.IProvider;
import fr.paris.lutece.plugins.workflowcore.service.provider.InfoMarker;
import fr.paris.lutece.portal.service.file.FileService;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class represents a provider for a {@link Forms} object
 *
 */
public class FormsProvider implements IProvider
{
  
    // FIELDS
    private final String _strCustomerEmail;
    private final String _strConnectionId;
    private final String _strCustomerId;
    private final String _strCustomerPhoneNumber;
    private final String _strDemandReference;
    private final String _strDemandTypeId;
    private final String _strProviderId;
    private final String _strProvideDemandeId;
    private final String _strBaseUrl;
    private final int _nIdFormResponse;
    private final HttpServletRequest _request;
    
    // PROPERTIES
    private static final String MARK_URL_FO_RESPONSE = "url_fo_forms_response_detail";
    private static final String MARK_CREATION_DATE = "creation_date";
    private static final String MARK_UPDATE_DATE = "update_date";
    private static final String MARK_STATUS = "status";
    private static final String MARK_STATUS_UPDATE_DATE = "update_date_status";
    private static final String MARK_URL_FO_FILES_LINK = "url_fo_forms_files_link";
    
    // PARAMETERS
    public static final String PARAMETER_VIEW_FORM_RESPONSE_DETAILS = "view_form_response_details";
    public static final String PARAMETER_VIEW_FORM_RESPONSE_DETAILS_FO = "formResponseView";
    public static final String PARAMETER_ID_FORM_RESPONSES = "id_form_response";
    public static final String PARAMETER_ID_FORM_RESPONSES_FO = "id_response";
    public static final String PARAMETER_PAGE_FORM_RESPONSE = "formsResponse";
    private static final String PARAMETER_VIEW_FORM_FILES_LINK_FO = "formFileView";


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
    	  // Get the form response from the resourceHistory
        FormResponse formResponse = FormResponseHome.findByPrimaryKey( resourceHistory.getIdResource( ) );
        _nIdFormResponse= formResponse.getId( );
        _strBaseUrl= AppPathService.getBaseUrl( request );

        _strProviderId= strProviderId;
        // Load the mapping manager
        NotifygruMappingManager mapping = NotifygruMappingManagerHome.findByPrimaryKey( ProviderManagerUtil.buildCompleteProviderId( strProviderManagerId, String.valueOf( formResponse.getFormId( ) )) );

        if ( mapping == null )
        {
            throw new AppException(
                    "No mapping found for the form " + formResponse.getFormId( ) + ". Please check the configuration of the module-forms-mappingmanager."  );
        }
        List<FormQuestionResponse> formQuestionresponseList = FormQuestionResponseHome.getFormQuestionResponseListByFormResponse( resourceHistory.getIdResource( )  );

        _strCustomerEmail = getFormResponseStringValue(formQuestionresponseList, mapping.getEmail( ), formResponse );
        _strConnectionId =  getFormResponseStringValue( formQuestionresponseList, mapping.getConnectionId( ), formResponse );        
        _strCustomerId = getFormResponseStringValue( formQuestionresponseList, mapping.getCustomerId( ), formResponse );      
        _strCustomerPhoneNumber = getFormResponseStringValue( formQuestionresponseList, mapping.getMobilePhoneNumber( ), formResponse );
        _strDemandReference =  getFormResponseStringValue(formQuestionresponseList, mapping.getDemandReference( ), formResponse );
        _strDemandTypeId = String.valueOf( mapping.getDemandeTypeId( ) );
        _strProvideDemandeId= String.valueOf( formResponse.getId( ) );
        _request = request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public String provideDemandId() {
		
		return _strProvideDemandeId;
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
        return AppPropertiesService.getProperty( Constants.PROPERTY_SMS_SENDER_NAME );
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
     * {@inheritDoc}
     */
    @Override
    public Collection<InfoMarker> provideMarkerValues( )
    {
        Collection<InfoMarker> result = new ArrayList<>( );

        List<FormQuestionResponse> listFormQuestionResponse = FormQuestionResponseHome.getFormQuestionResponseListByFormResponse( _nIdFormResponse );

        Map<Integer, InfoMarker> markers = new HashMap<>( );
        if(_strProviderId.startsWith( Constants.ALL_FORMS ) ) 
    	{
	        for ( FormQuestionResponse formQuestionResponse : listFormQuestionResponse )
	        {
	            InfoMarker notifyMarker = markers.computeIfAbsent( formQuestionResponse.getQuestion( ).getId( ),
	                     k  -> new InfoMarker( formQuestionResponse.getQuestion( ).getCode() ) );
	            setValue(  notifyMarker, getValue( formQuestionResponse ) );
	           
	        }
    	}else {
    		for ( FormQuestionResponse formQuestionResponse : listFormQuestionResponse )
	        {
	            InfoMarker notifyMarker = markers.computeIfAbsent( formQuestionResponse.getQuestion( ).getId( ),
	                     k  -> new InfoMarker( Constants.MARK_POSITION + formQuestionResponse.getQuestion( ).getId( ) ) );
	            setValue(  notifyMarker, getValue( formQuestionResponse ) );
	        }
    	}
        result.addAll( markers.values( ) );
        
        FormResponse formResponse = FormResponseHome.findByPrimaryKey( _nIdFormResponse );

        InfoMarker notifyMarkerUrl = new InfoMarker( Constants.MARK_URL_ADMIN_RESPONSE );
        UrlItem url = new UrlItem( _strBaseUrl + MultiviewFormResponseDetailsJspBean.CONTROLLER_JSP_NAME_WITH_PATH );
        url.addParameter( FormsConstants.PARAMETER_TARGET_VIEW, Constants.PARAMETER_VIEW_FORM_RESPONSE_DETAILS );
        url.addParameter( Constants.PARAMETER_ID_FORM_RESPONSES, _nIdFormResponse );
        notifyMarkerUrl.setValue( url.getUrl( ) );
        result.add( notifyMarkerUrl );
        
        InfoMarker notifyMarkerFOUrl = new InfoMarker( MARK_URL_FO_RESPONSE );
        UrlItem urlFO = new UrlItem( AppPathService.getProdUrl( _request ) + AppPathService.getPortalUrl( ) );
        urlFO.addParameter( FormsConstants.PARAMETER_PAGE, PARAMETER_PAGE_FORM_RESPONSE );
        urlFO.addParameter( FormsConstants.PARAMETER_TARGET_VIEW, PARAMETER_VIEW_FORM_RESPONSE_DETAILS_FO );
        urlFO.addParameter( PARAMETER_ID_FORM_RESPONSES_FO, _nIdFormResponse );
        notifyMarkerFOUrl.setValue( urlFO.getUrl( ) );
        result.add( notifyMarkerFOUrl );

        InfoMarker notifyMarkerFOFileUrl = new InfoMarker( MARK_URL_FO_FILES_LINK );
        UrlItem urlFilesLinkFo = new UrlItem( AppPathService.getProdUrl( _request ) + AppPathService.getPortalUrl( ) );
        urlFilesLinkFo.addParameter( FormsConstants.PARAMETER_PAGE, PARAMETER_PAGE_FORM_RESPONSE );
        urlFilesLinkFo.addParameter( FormsConstants.PARAMETER_TARGET_VIEW, PARAMETER_VIEW_FORM_FILES_LINK_FO );
        urlFilesLinkFo.addParameter( PARAMETER_ID_FORM_RESPONSES_FO, _nIdFormResponse );
        notifyMarkerFOFileUrl.setValue( urlFilesLinkFo.getUrl( ) );
        result.add( notifyMarkerFOFileUrl );
        
        InfoMarker creationDateMarker = new InfoMarker( MARK_CREATION_DATE );
        creationDateMarker.setValue( formResponse.getCreation( ).toString( ) );
        result.add( creationDateMarker );
        
        InfoMarker updateDateMarker = new InfoMarker( MARK_UPDATE_DATE );
        updateDateMarker.setValue( formResponse.getCreation( ).toString( ) );
        result.add( updateDateMarker );
        
        InfoMarker statusMarker = new InfoMarker( MARK_STATUS );
        statusMarker.setValue( String.valueOf( formResponse.isPublished( ) ) );
        result.add( statusMarker );
        
        InfoMarker updateStatusDateMarker = new InfoMarker( MARK_STATUS_UPDATE_DATE );
        updateStatusDateMarker.setValue( formResponse.getUpdateStatus( ).toString( ) );
        result.add( updateStatusDateMarker );
        
        return result;
    }
    /**
     * Get value of formQuestionResponse
     * @param formQuestionResponse
     * @return value
     */
    private String getValue(FormQuestionResponse formQuestionResponse) {
    	
    	 IEntryTypeService entryTypeService = EntryTypeServiceManager.getEntryTypeService( formQuestionResponse.getQuestion( ).getEntry( ) );
         String value = "";
         if ( entryTypeService instanceof EntryTypeComment )
         {
             Entry entry = formQuestionResponse.getQuestion( ).getEntry( );
             Field fieldFile = entry.getFieldByCode( IEntryTypeService.FIELD_DOWNLOADABLE_FILE );
             if ( fieldFile != null )
             {
                 IFileStoreServiceProvider fileStoreprovider = FileService.getInstance( ).getFileStoreServiceProvider( "formsDatabaseFileStoreProvider" );

                 Map<String, String> additionnalData = new HashMap<>( );
                 additionnalData.put( FileService.PARAMETER_RESOURCE_ID, String.valueOf( entry.getIdResource( ) ) );
                 additionnalData.put( FileService.PARAMETER_RESOURCE_TYPE, Form.RESOURCE_TYPE );
                 additionnalData.put( FileService.PARAMETER_PROVIDER, fileStoreprovider.getName( ) );

                 value = fileStoreprovider.getFileDownloadUrlFO( fieldFile.getValue( ), additionnalData );
             }
         }
         else if ( CollectionUtils.isNotEmpty( formQuestionResponse.getEntryResponse( ) ) )
         {
             	  value = formQuestionResponse.getEntryResponse( ).stream( ).map(
                           response -> entryTypeService.getResponseValueForRecap( formQuestionResponse.getQuestion( ).getEntry( ), null, response, null ) )
                           .filter( StringUtils::isNotEmpty ).collect( Collectors.joining( ", " ) );
         }
         return value;
    }
    /**
     * Set value in infoMarker
     * @param notifyMarker the InfoMarker
     * @param value the value to set
     */
    private void setValue( InfoMarker notifyMarker, String value ) {
    	if ( notifyMarker.getValue( ) == null )
        {
            notifyMarker.setValue( value );
        }
        else
        {
            notifyMarker.setValue( notifyMarker.getValue( ) + "<br>" + value );
        }
        AppLogService.debug( "Adding infomarker {} = {}", notifyMarker.getMarker( ),  notifyMarker.getValue( ) );
    }
    
   
    /**
     * Get the form response string value with given nIdResponse and given formResponse
     * @param formQuestionresponseList
     * 			The form Question response List
     * @param nIdResponse
     *            The id response
     * @param formResponse
     *            The form response
     * @return the form response String value
     */
    private String getFormResponseStringValue( List<FormQuestionResponse> formQuestionresponseList,  int nIdResponse, FormResponse formResponse )
    {
    	List<Response> responseList =formQuestionresponseList.stream( ).filter( response -> response.getQuestion( ).getId( ) == nIdResponse ).findFirst( ).map( FormQuestionResponse::getEntryResponse )
                .orElse( null );

        if ( CollectionUtils.isNotEmpty( responseList ) )
        {
            return responseList.get( 0 ).getToStringValueResponse( );
        }
        return StringUtils.EMPTY;
    }

}
