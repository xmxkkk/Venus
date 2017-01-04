package venus.helper.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommandProperty {
	
	@Value("${command.sct}")
	String commandSct;
	@Value("${command.sct.cache}")
	String commandSctCache;
	@Value("${command.dt}")
	String commandDt;
	@Value("${command.lst}")
	String commandLst;
	@Value("${command.ct}")
	String commandCt;
	@Value("${command.shutdown}")
	String commandShutdown;
	@Value("${command.sct.force}")
	String commandSctForce;
	@Value("${command.lst.id}")
	int commandLstId;
	@Value("${command.lst.json}")
	String commandLstJson;
	@Value("${command.lst.force}")
	String commandLstForce;
	
	
	public String getCommandLstForce() {
		return commandLstForce;
	}
	public void setCommandLstForce(String commandLstForce) {
		this.commandLstForce = commandLstForce;
	}
	public String getCommandLstJson() {
		return commandLstJson;
	}
	public void setCommandLstJson(String commandLstJson) {
		this.commandLstJson = commandLstJson;
	}
	public int getCommandLstId() {
		return commandLstId;
	}
	public void setCommandLstId(int commandLstId) {
		this.commandLstId = commandLstId;
	}
	public String getCommandSctForce() {
		return commandSctForce;
	}
	public void setCommandSctForce(String commandSctForce) {
		this.commandSctForce = commandSctForce;
	}
	public String getCommandSct() {
		return commandSct;
	}
	public void setCommandSct(String commandSct) {
		this.commandSct = commandSct;
	}
	public String getCommandSctCache() {
		return commandSctCache;
	}
	public void setCommandSctCache(String commandSctCache) {
		this.commandSctCache = commandSctCache;
	}
	public String getCommandDt() {
		return commandDt;
	}
	public void setCommandDt(String commandDt) {
		this.commandDt = commandDt;
	}
	public String getCommandLst() {
		return commandLst;
	}
	public void setCommandLst(String commandLst) {
		this.commandLst = commandLst;
	}
	public String getCommandCt() {
		return commandCt;
	}
	public void setCommandCt(String commandCt) {
		this.commandCt = commandCt;
	}
	public String getCommandShutdown() {
		return commandShutdown;
	}
	public void setCommandShutdown(String commandShutdown) {
		this.commandShutdown = commandShutdown;
	}
	@Override
	public String toString() {
		return "CommandProperty [commandSct=" + commandSct + ", commandSctCache=" + commandSctCache + ", commandDt="
				+ commandDt + ", commandLst=" + commandLst + ", commandCt=" + commandCt + ", commandShutdown="
				+ commandShutdown + ", commandSctForce=" + commandSctForce + ", commandLstId=" + commandLstId
				+ ", commandLstJson=" + commandLstJson + ", commandLstForce=" + commandLstForce + "]";
	}
	
}
