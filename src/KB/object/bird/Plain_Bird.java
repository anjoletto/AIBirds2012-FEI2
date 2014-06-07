/**This software is distributed under terms of the BSD license. See the LICENSE file for details.**/
package KB.object.bird;

import KB.object.Appearance;

public class Plain_Bird extends Bird {

	public Plain_Bird(Appearance appearnce) {
		super(appearnce);
		this.setHasChildren(true);
		// TODO Auto-generated constructor stub
	}
	public String hierarchy() {
		return super.hierarchy() + "/Plain_Bird";
	}
	public static String title()
	{
		return "Plain_Bird";
	}
}
