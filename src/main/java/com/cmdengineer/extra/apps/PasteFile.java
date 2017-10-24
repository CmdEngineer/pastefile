package com.cmdengineer.extra.apps;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.omg.DynamicAny.NameValuePairHelper;

import com.cmdengineer.extra.PastebinAPI;
import com.cmdengineer.extra.utills.HTTP;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.app.Dialog;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.Label;
import com.mrcrayfish.device.api.app.component.TextField;
import com.mrcrayfish.device.api.app.listener.ClickListener;
import com.mrcrayfish.device.api.io.Drive;
import com.mrcrayfish.device.api.io.File;
import com.mrcrayfish.device.api.utils.OnlineRequest;
import com.mrcrayfish.device.core.io.FileSystem;
import com.mrcrayfish.device.core.io.action.FileAction;
import com.mrcrayfish.device.programs.system.ApplicationFileBrowser;

import io.netty.util.internal.StringUtil;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringUtils;

public class PasteFile extends Application {

	public PasteFile() {
	    super();
	}

	Layout main, upload, download;
	HTTP http;
	
	Button btnUpload, btnDownload;
	
	Label upload_txtUpload, upload_txtCode;
	TextField upload_txfCode;
	
	Label download_txtCode;
	TextField download_txfCode;
	
	
	int width = 80, height = 80;
	String code;
	@Override
	public void init() {
		
		// Main Layout:
		http = new HTTP();
		main = new Layout(width, height);
		
		btnUpload = new Button("Upload", 5, 5, width-10, height/2-10);
		main.addComponent(btnUpload);
		
		btnDownload = new Button("Download", 5, height/2+5, width-10, height/2-10);
		main.addComponent(btnDownload);

		// Upload Layout:
		upload = new Layout(220, 40);
		
		upload_txtUpload = new Label("Your file has been uploaded successfully!", 2, 1);
		upload_txtUpload.setTextColour(Color.GREEN);
		upload.addComponent(upload_txtUpload);
		
		upload_txtCode = new Label("File Code:", 220/2 - 70, 18);
		upload.addComponent(upload_txtCode);
		
		upload_txfCode = new TextField(220/2-20, 15, 60);
		upload_txfCode.setEditable(false);
		upload.addComponent(upload_txfCode);
		
		// Download Layout:
		
		download = new Layout(160, 25);
		
		download_txfCode = new TextField(160/2-5, 5, 60);
		download.addComponent(download_txfCode);
		
		download_txtCode = new Label("File Code:",  160/2 - 60, 8);
		download.addComponent(download_txtCode);
		
		
		// Events:
		btnUpload.setClickListener(new ClickListener() {
			@Override
			public void onClick(Component component, int mouse) {
				openFile(true, upload);
			}
		});
		
		btnDownload.setClickListener(new ClickListener() {
			@Override
			public void onClick(Component component, int success) {
				PasteFile.this.setCurrentLayout(download);
			}
		});
		this.setCurrentLayout(main);
	}

	@Override
	public void handleKeyTyped(char character, int code) {
		super.handleKeyTyped(character, code);
	    String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";    
		if(download_txfCode.getText().length() == 8){
			for(int i = 0; i < 7; i++){
				if(!possible.contains(download_txfCode.getText().substring(i, i + 1))){
					return;
				}
			}
			downloadFile(download_txfCode.getText());
		}
	}
	
	public void downloadFile(String text){
		try{
			if(text.length() == 8){
				//String pp = http.sendGet("52.56.208.150/" + text);
				String pp = http.sendGet("http://52.56.208.150/" + text);
				NBTTagCompound nbt = new NBTTagCompound();
				NBTTagCompound data = new NBTTagCompound();
				try {
					data = JsonToNBT.getTagFromJson(pp.split("<=Data=>")[1]);
					nbt = JsonToNBT.getTagFromJson(pp.split("<=Data=>")[0]);

				} catch (NBTException e) {
					e.printStackTrace();
				}
				File newFile = new File(nbt.getString("name"), nbt.getString("app"), data);
				Dialog.SaveFile dialog = new Dialog.SaveFile(PasteFile.this, newFile);
				dialog.setResponseHandler((success, file) -> {
					if(success){
						return true;
					}
					return false;
				});
				openDialog(dialog);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void openFile(boolean upload, Layout layout){
		Dialog.OpenFile dialog = new Dialog.OpenFile(PasteFile.this);
		openDialog(dialog);
		dialog.setResponseHandler(new Dialog.ResponseHandler<File>() {
			@Override
			public boolean onResponse(boolean valid, File file) {
				if(upload) uploadFile(file);
				PasteFile.this.setCurrentLayout(layout);
				return valid;
			}
		});
	}

	public void uploadFile(File file){
		try{
			if(file != null){
				String fileInfo = getFileInfo(file);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("info", fileInfo));
				params.add(new BasicNameValuePair("json", file.getData().toString()));
				//http.sendPost("52.56.208.150", params);
				String text = http.sendPost("http://52.56.208.150/", params);
				upload_txfCode.setText(text);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getFileInfo(File file){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("name", file.getName());
		nbt.setString("app", file.getOpeningApp());
		return nbt.toString();
	}
	
	@Override
	public void load(NBTTagCompound nbt) {
		
	}

	@Override
	public void save(NBTTagCompound nbt) {
		
	}

}
