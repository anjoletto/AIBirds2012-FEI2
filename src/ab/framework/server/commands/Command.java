/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package ab.framework.server.commands;

import org.json.simple.JSONObject;

public interface Command<T> {
	public String getCommandName();
	public JSONObject getJSON();
	public T gotResponse(JSONObject data);
}
