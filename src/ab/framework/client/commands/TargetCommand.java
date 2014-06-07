/**
 * This software is distributed under terms of the BSD license. See the LICENSE
 * file for details.*
 */
package ab.framework.client.commands;

import org.json.simple.JSONObject;

import ab.framework.server.commands.Command;

public class TargetCommand implements Command<Object> {
    
    private int x;
    private int y;
    
    public TargetCommand(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String getCommandName() {
        return "target";
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public JSONObject getJSON() {
        JSONObject o = new JSONObject();
        
        o.put("x", x);
        o.put("y", y);
        return o;
    }
    
    @Override
    public Object gotResponse(JSONObject data) {
        return new Object();
    }
}
