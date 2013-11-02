package vva.xinyu126.msgspf;


public class SMSEntity {
	private long id;
	private long threadID;
	private String from;
	private String person;
	private String date;
	private String protocol;
	private int isRead;
	private int status;
	private String type;
	private String replyPathPresent;
	private String subject;
	private String body;
	private String serviceCenter;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getThreadID() {
		return threadID;
	}
	public void setThreadID(long threadID) {
		this.threadID = threadID;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public int isRead() {
		return isRead;
	}
	public void setRead(int isRead) {
		this.isRead = isRead;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReplyPathPresent() {
		return replyPathPresent;
	}
	public void setReplyPathPresent(String replyPathPresent) {
		this.replyPathPresent = replyPathPresent;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getServiceCenter() {
		return serviceCenter;
	}
	public void setServiceCenter(String serviceCenter) {
		this.serviceCenter = serviceCenter;
	}
}
