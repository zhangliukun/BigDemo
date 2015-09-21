package com.aidingmao.database;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class DatabaseGenerator {
	
	public static final int DATABASE_VERSION = 2;

	public static void main(String[] args) throws Exception{
		Schema schema = new Schema(DATABASE_VERSION, "com.shinemo.framework.database.generator");  
		  
		addDiskDir(schema);
		addDiskShare(schema);
		addDiskOrgShare(schema);
		addOgr(schema);
		addDepartment(schema);
		addUser(schema);
		addConversation(schema);
		addGroupMember(schema);
		addSingleMesage(schema);
		addGroupMesage(schema);
		addMyUser(schema);
		addSchedule(schema);
		addGroups(schema);
		addSingle(schema);
		addNote(schema);
		addAnnouncementList(schema);
		addPhone(schema);
		addPublicService(schema);
        new DaoGenerator().generateAll(schema, "../Shinemo/src"); 
	}
	
	private static void addSchedule(Schema schema){
		Entity schedule = schema.addEntity("Schedule");
		schedule.addLongProperty("sid").primaryKey();
		schedule.addLongProperty("uid");
		schedule.addIntProperty("type");
		schedule.addStringProperty("content");
		schedule.addLongProperty("scheduleTime");
		schedule.addStringProperty("createTime");
		schedule.addStringProperty("hostName");
		schedule.addIntProperty("alertTime");
		schedule.addIntProperty("alertType");
		schedule.addIntProperty("priority");
		schedule.addStringProperty("mediaLocalPath");
		schedule.addStringProperty("attachmentDigest");
	}
	
	private static void addNote(Schema schema){
		Entity conver = schema.addEntity("Note");
		conver.addLongProperty("taskId").primaryKey();
		conver.addLongProperty("uid");
		conver.addStringProperty("publisherName");
		conver.addStringProperty("content");
		conver.addIntProperty("unReadCount");
		conver.addIntProperty("replayCount");
		conver.addIntProperty("attenderCounts");
		conver.addStringProperty("myStatus");
		conver.addLongProperty("releaseTime");
		conver.addBooleanProperty("isSyncToMyDate");
		conver.addLongProperty("scheduleTime");
		conver.addStringProperty("replayContent");
		conver.addBooleanProperty("hasNewReplay");
		conver.addStringProperty("recordUrl");
		conver.addBooleanProperty("hasRecord");
		conver.addIntProperty("type");
	}
	
	private static void addAnnouncementList(Schema schema){
		Entity conver = schema.addEntity("AnnouncementList");
		conver.addLongProperty("announcementId").primaryKey();
		conver.addLongProperty("orgId");
		conver.addStringProperty("title");
		conver.addLongProperty("releaseTime");
		conver.addStringProperty("content");
		conver.addBooleanProperty("hasRead");
		conver.addIntProperty("type");
		
		
	}
	
	private static void addConversation(Schema schema){
		Entity conver = schema.addEntity("Conversation");
		conver.addStringProperty("cid").primaryKey();
		conver.addIntProperty("conversationType").getProperty();
		conver.addStringProperty("name");
		conver.addStringProperty("urlList");
		conver.addStringProperty("draft");
		conver.addBooleanProperty("isAt");
		conver.addStringProperty("lastMessage");
		conver.addIntProperty("unreadCount");
		conver.addLongProperty("lastModifyTime");
		conver.addBooleanProperty("isNotification");
		conver.addBooleanProperty("isTop");
		conver.addStringProperty("chatBackgroud");
		conver.addStringProperty("groupToken");
		conver.addLongProperty("memberVersion");
	}
	
	private static void addGroups(Schema schema){
		Entity conver = schema.addEntity("Group");
		conver.addLongProperty("groupId").primaryKey();
		conver.addStringProperty("name");
		conver.addStringProperty("urlList");
		conver.addBooleanProperty("isNotification");
		conver.addBooleanProperty("isTop");
		conver.addStringProperty("chatBackgroud");
		conver.addStringProperty("groupToken");
		conver.addStringProperty("createId");
		conver.addLongProperty("memberVersion");
		conver.addIntProperty("memberCount");
		conver.addStringProperty("pinyin");
	}
	
	private static void addSingle(Schema schema){
		Entity conver = schema.addEntity("Single");
		conver.addStringProperty("uid").primaryKey();
		conver.addBooleanProperty("isNotification");
		conver.addBooleanProperty("isTop");
		conver.addStringProperty("chatBackgroud");
	}
	
	private static void addGroupMember(Schema schema){
		Entity conver = schema.addEntity("GroupMember");
		conver.addIdProperty().autoincrement();
		Property cid = conver.addLongProperty("cid").getProperty();
		Property uid = conver.addStringProperty("uid").getProperty();
		conver.addStringProperty("userName");
		
		Index index = new Index();
		index.addProperty(uid);
		index.addProperty(cid);
		index.makeUnique();
		conver.addIndex(index);
	}
	
	private static void addSingleMesage(Schema schema){
		Entity message = schema.addEntity("SingleMessage");
		message.addLongProperty("mid").primaryKey();
		Property seqId = message.addLongProperty("seqId").getProperty();
		Property cid = message.addStringProperty("cid").getProperty();
		message.addStringProperty("uid");
		message.addStringProperty("name");
		message.addIntProperty("type");
		message.addStringProperty("content");
		message.addLongProperty("time");
		message.addStringProperty("extra");
		message.addIntProperty("unreadcount");
		message.addIntProperty("status");
		message.addBooleanProperty("needBack");
		message.addBooleanProperty("isRead");
		message.addBooleanProperty("isReadSuccess");
		
		Index index = new Index();
		index.addProperty(seqId);
		index.addProperty(cid);
		index.makeUnique();
		message.addIndex(index);
	}
	
	private static void addGroupMesage(Schema schema){
		Entity message = schema.addEntity("GroupMessage");
		message.addLongProperty("mid").primaryKey();
		Property seqId = message.addLongProperty("seqId").getProperty();
		Property cid = message.addStringProperty("cid").getProperty();
		message.addStringProperty("uid");
		message.addStringProperty("name");
		message.addIntProperty("type");
		message.addStringProperty("content");
		message.addLongProperty("time");
		message.addStringProperty("extra");
		message.addIntProperty("unreadcount");
		message.addIntProperty("status");
		message.addBooleanProperty("needBack");
		message.addBooleanProperty("isRead");
		message.addBooleanProperty("isReadSuccess");
		
		Index index = new Index();
		index.addProperty(seqId);
		index.addProperty(cid);
		index.makeUnique();
		message.addIndex(index);
	}
	
	private static void addOgr(Schema schema){
		Entity org = schema.addEntity("Organization");
		org.addLongProperty("id").primaryKey();
		org.addLongProperty("userDataVersion");
		org.addLongProperty("lastOperateStamp");
		org.addStringProperty("avatar");
		org.addStringProperty("name");
	}


	private static void addDepartment(Schema schema){
		Entity depart = schema.addEntity("Department");
		depart.addIdProperty().autoincrement();
		Property orgId = depart.addLongProperty("orgId").getProperty();
		Property departId = depart.addLongProperty("departmentId").getProperty();
		Property parentId = depart.addLongProperty("parentId").getProperty();
		depart.addLongProperty("userCounts");
		depart.addIntProperty("sequence");
		depart.addStringProperty("name");
		depart.addStringProperty("description");
		depart.addStringProperty("parentIds");
		depart.addStringProperty("orgName");

		Index index = new Index();
		index.addProperty(departId);
		index.addProperty(orgId);
		index.makeUnique();
		depart.addIndex(index);

		Index orgIdIndex = new Index();
		orgIdIndex.addProperty(orgId);
		depart.addIndex(orgIdIndex);

		Index parentIdIndex = new Index();
		parentIdIndex.addProperty(parentId);
		parentIdIndex.addProperty(orgId);
		depart.addIndex(parentIdIndex);
	}

	private static void addUser(Schema schema){
		Entity user = schema.addEntity("User");
		user.addIdProperty().autoincrement();
		Property uid = user.addLongProperty("uid").getProperty();
		Property orgId = user.addLongProperty("orgId").getProperty();
		Property departId = user.addLongProperty("departmentId").getProperty();
		user.addIntProperty("sequence");
		Property mobile = user.addStringProperty("mobile").getProperty();
		user.addStringProperty("title");
		Property name = user.addStringProperty("name").getProperty();
		user.addStringProperty("pinyin");
		user.addIntProperty("sex");
		user.addStringProperty("email");
		user.addStringProperty("homePhone");
		user.addStringProperty("personalCellPhone");
		user.addStringProperty("shortNum");
		user.addStringProperty("shortNum2");
		user.addStringProperty("workPhone");
		user.addStringProperty("workPhone2");
		user.addStringProperty("virtualCellPhone");
		user.addStringProperty("remark");
		user.addBooleanProperty("isAllowLogin");
		user.addStringProperty("virtualCode");
		user.addStringProperty("fax");
		user.addStringProperty("shortPinyin");
		user.addStringProperty("customField");
		user.addStringProperty("privilege");
		user.addStringProperty("orgName");


		Index index = new Index();
		index.addProperty(uid);
		index.addProperty(departId);
		index.addProperty(orgId);
		index.makeUnique();
		user.addIndex(index);

		Index orgIdIndex = new Index();
		orgIdIndex.addProperty(orgId);
		user.addIndex(orgIdIndex);

		Index nameIndex = new Index();
		nameIndex.addProperty(name);
		user.addIndex(nameIndex);

		Index mobileIndex = new Index();
		mobileIndex.addProperty(mobile);
		user.addIndex(mobileIndex);


		Index uidIndex = new Index();
		uidIndex.addProperty(uid);
		user.addIndex(uidIndex);

		Index departIndex = new Index();
		departIndex.addProperty(departId);
		departIndex.addProperty(orgId);
		user.addIndex(departIndex);


		Index groupIndex = new Index();
		groupIndex.addProperty(uid);
		groupIndex.addProperty(orgId);
		user.addIndex(groupIndex);
	}
	
	private static void addMyUser(Schema schema){
		Entity user = schema.addEntity("MyInfo");
		user.addIdProperty().autoincrement();
		Property uid = user.addLongProperty("uid").getProperty();
		Property orgId = user.addLongProperty("orgId").getProperty();
		Property departId = user.addLongProperty("departmentId").getProperty();
		user.addStringProperty("orgName");
		user.addStringProperty("departName");
		user.addIntProperty("sequence");
		user.addStringProperty("mobile");
		user.addStringProperty("title");
		user.addStringProperty("name");
		user.addStringProperty("pinyin");
		user.addIntProperty("sex");
		user.addStringProperty("email");
		user.addStringProperty("homePhone");
		user.addStringProperty("personalCellPhone");
		user.addStringProperty("shortNum");
		user.addStringProperty("shortNum2");
		user.addStringProperty("workPhone");
		user.addStringProperty("workPhone2");
		user.addStringProperty("virtualCellPhone");
		user.addStringProperty("remark");
		user.addBooleanProperty("isAllowLogin");
		user.addStringProperty("departmentIds");
		user.addStringProperty("virtualCode");
		user.addStringProperty("fax");
		user.addStringProperty("shortPinyin");
		user.addStringProperty("customField");
		user.addStringProperty("privilege");
		
		Index index = new Index();
		index.addProperty(uid);
		index.addProperty(departId);
		index.addProperty(orgId);
		index.makeUnique();
		user.addIndex(index);
	}
	
	private static void addDiskDir(Schema schema) {  
        Entity diskdir = schema.addEntity("CloudDisk");
        diskdir.addStringProperty("dirId").primaryKey();
        diskdir.addStringProperty("filePathSource");
        diskdir.addStringProperty("parentDirId");
        diskdir.addStringProperty("objectId");
        diskdir.addLongProperty("orgId");
        diskdir.addStringProperty("path");
        diskdir.addStringProperty("type");
        diskdir.addStringProperty("fileName");
        diskdir.addStringProperty("namespace");
        diskdir.addStringProperty("mimeType");
        diskdir.addLongProperty("fileSize");
        diskdir.addLongProperty("fileCreatedTime");
        diskdir.addLongProperty("fileUpdateTime");
        diskdir.addIntProperty("dirTrash");
        diskdir.addIntProperty("uploadSteps");
        diskdir.addStringProperty("downloadUrl");
        diskdir.addStringProperty("state");
    }
	
	private static void addDiskShare(Schema schema) {
		Entity diskdir = schema.addEntity("DiskShare");
		diskdir.addStringProperty("shareId").primaryKey();
        diskdir.addStringProperty("objectId");
        diskdir.addStringProperty("objectType");
        diskdir.addStringProperty("objectName");
        diskdir.addIntProperty("objectSize"); 
        diskdir.addStringProperty("fromUserId");
        diskdir.addStringProperty("namespace");
        diskdir.addStringProperty("fromUserName");
        diskdir.addLongProperty("sharedTime");
        diskdir.addStringProperty("toUser");
        diskdir.addStringProperty("state");
	}
	
	private static void addDiskOrgShare(Schema schema) {
		Entity diskdir = schema.addEntity("DiskOrgShare");
        diskdir.addStringProperty("id").primaryKey();
        diskdir.addStringProperty("type");
        diskdir.addStringProperty("name");
        diskdir.addLongProperty("fileSize"); 
        diskdir.addStringProperty("mimeType");
        diskdir.addStringProperty("digest");
        diskdir.addStringProperty("parentDirId");
        diskdir.addLongProperty("compressedType");
        diskdir.addStringProperty("createdTime");
        diskdir.addStringProperty("updatedTime");
        diskdir.addStringProperty("state");
        diskdir.addStringProperty("namespace");
	}
	
	private static void addPhone(Schema schema) {
		Entity entity = schema.addEntity("AddressBook");
		entity.addIdProperty().autoincrement();
		entity.addLongProperty("contactId");
		entity.addStringProperty("name");
		entity.addStringProperty("number");
		entity.addStringProperty("sortKey");
		entity.addIntProperty("version");

	}

	private static void addPublicService(Schema schema) {
		Entity entity = schema.addEntity("PublicService");
		entity.addLongProperty("id").primaryKey();
		entity.addStringProperty("service");
		entity.addStringProperty("subService");
		entity.addStringProperty("phone");
	}
	
}
