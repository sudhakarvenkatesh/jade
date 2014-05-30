package com.sos.jade.backgroundservice.view;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import sos.ftphistory.db.JadeFilesDBItem;
import sos.ftphistory.db.JadeFilesDBLayer;
import sos.ftphistory.db.JadeFilesHistoryDBItem;

import com.sos.jade.backgroundservice.enums.JadeFileColumns;
import com.sos.jade.backgroundservice.enums.TableType;
import com.sos.jade.backgroundservice.view.components.JadeFilesHistoryTable;
import com.sos.jade.backgroundservice.view.components.JadeFilesTable;
import com.sos.jade.backgroundservice.view.components.JadeFilterLayout;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class MainView extends CustomComponent {
	
	private static final long serialVersionUID = 6368275374953898482L;
	private String absolutePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	private String configurationFilename = absolutePath + "/WEB-INF/classes/hibernate.cfg.xml";
	private JadeFilesDBLayer jadeFilesDBLayer;
	private List<JadeFilesHistoryDBItem> historyItems;
	private List<JadeFilesDBItem> fileItems;
	private Panel scrollableHistoryPanel;
	private Panel scrollableFilePanel;
	private CssLayout cssFileTableLayout;
	private CssLayout cssHistoryTableLayout;
	private JadeFilesHistoryTable tblHistory;
	private JadeFilesTable tblFiles;
	private Item markedRow;
	private JadeFilterLayout filterLayout;

	public MainView() {
		initJadeFileData();
		initComponents();
		
        tblFiles.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if (markedRow == null || !markedRow.equals(event.getItemId())){
					// then query all depending jadeHistoryFilesItems
					if(event.getItem().getItemProperty(JadeFileColumns.ID.getName()) != null){
						Long id = (Long)event.getItem().getItemProperty(JadeFileColumns.ID.getName()).getValue();
						try {
							historyItems = jadeFilesDBLayer.getFilesHistoryById(id);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						tblHistory.populateDatasource(historyItems);
					}
				}
				toggleTableVisiblity(event.getItem());
			}
		});
	}
	
	private void initJadeFileData(){
		File configFile = new File(configurationFilename);
		jadeFilesDBLayer = new JadeFilesDBLayer(configFile);
		// first query for the jadeFilesItems
        jadeFilesDBLayer.initSession();
        try {
 			fileItems = jadeFilesDBLayer.getFiles();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private Table initTable(TableType tableType, Object data){
		switch(tableType){
		case FILE:
			return new JadeFilesTable((List<JadeFilesDBItem>)data);
		case HISTORY:
			return new JadeFilesHistoryTable((List<JadeFilesHistoryDBItem>)data);
		}
		return null;
		
	}
	
	private void initTables(){
        tblFiles = (JadeFilesTable)initTable(TableType.FILE, fileItems);
        tblHistory = (JadeFilesHistoryTable)initTable(TableType.HISTORY, null);
        scrollableFilePanel.setContent(tblFiles);
        scrollableHistoryPanel.setContent(tblHistory);
//        tblFiles.setVisible(false);
        tblHistory.setVisible(false);
	}
	
	private void initComponents(){
        final VerticalLayout vLayout = new VerticalLayout();
        vLayout.setMargin(true);
        vLayout.setSizeFull();
        setCompositionRoot(vLayout);

        final VerticalLayout vTitle = new VerticalLayout();
        vTitle.setHeight("70px");
        vLayout.addComponent(vTitle);

        final VerticalLayout vRest = new VerticalLayout();
        vRest.setSizeFull();

        vLayout.addComponent(vRest);
        vLayout.setExpandRatio(vTitle, 1);
        vLayout.setExpandRatio(vRest, 9);

        final HorizontalLayout hLayout = new HorizontalLayout();
        vTitle.addComponent(hLayout);

        final Image imgTitle = new Image();
		FileResource titleResource = new FileResource(new File(absolutePath + "/images/job_scheduler_rabbit_circle_60x60.gif"));
		imgTitle.setSource(titleResource);
        hLayout.addComponent(imgTitle);

        final Label lblTitle = new Label("JADE background service");
        lblTitle.setStyleName("jadeTitleLabel");
        hLayout.addComponent(lblTitle);

        filterLayout = new JadeFilterLayout(jadeFilesDBLayer);
        vRest.addComponent(filterLayout);
        
        cssFileTableLayout = initTableCssLayout();
        vRest.addComponent(cssFileTableLayout);

        cssHistoryTableLayout = initTableCssLayout();
        vRest.addComponent(cssHistoryTableLayout);
        
        scrollableFilePanel = initScrollablePanel("100%", "100%");
        cssFileTableLayout.addComponent(scrollableFilePanel);

        scrollableHistoryPanel = initScrollablePanel("100%", "100%"); 
        cssHistoryTableLayout.addComponent(scrollableHistoryPanel);

        vRest.setExpandRatio(filterLayout, 1);
		vRest.setExpandRatio(cssFileTableLayout, 1);
        vRest.setExpandRatio(cssHistoryTableLayout, 1);

        initTables();
	}
	
	private CssLayout initTableCssLayout(){
		CssLayout cssLayout = new CssLayout();
		cssLayout.setSizeFull();
		return cssLayout;
	}
	
	private Panel initScrollablePanel(String width, String height){
        Panel panel = new Panel();
		if (width != null && height != null){
			panel.setWidth(width);
			panel.setHeight(height);
		}else{
	        panel.setSizeFull();
		}
		panel.setStyleName("jadePanel");
		return panel;
	}
	
	private void toggleTableVisiblity(Item item){
		if(markedRow != null && markedRow.equals(item)){
			markedRow = null;
			tblHistory.setVisible(false);
		}else{
			markedRow = item;
			tblHistory.setVisible(true);
		}
	}
	
}
