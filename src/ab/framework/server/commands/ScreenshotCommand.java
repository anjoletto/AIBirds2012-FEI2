/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package ab.framework.server.commands;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;



// request a screenshot from the game
public class ScreenshotCommand implements Command<byte[]> {
	@Override
	public String getCommandName() {
		return "screenshot";
	}

	@Override
	public JSONObject getJSON() {
		return new JSONObject();
	}

	@Override
	public byte[] gotResponse(JSONObject data) {
		String imageStr = (String) data.get("data");
		imageStr = imageStr.split(",", 2)[1];
		byte[] imageBytes = Base64.decodeBase64(imageStr);
		return imageBytes;
	}
}
