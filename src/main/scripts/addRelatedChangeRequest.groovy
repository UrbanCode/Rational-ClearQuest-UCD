/**
 *   © Copyright IBM Corporation 2016.
 *   This is licensed under the following license.
 *   The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 *   U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.urbancode.air.plugin.rcq.ClearQuestHelper
import com.urbancode.plugin.helper.NewAirPluginTool

def apTool = new NewAirPluginTool(this.args[0], this.args[1]);
def props = apTool.getStepProperties();

String username = props['username'].trim()
String password = props['password']
String hostname = props['hostname'].trim()
String repo     = props['repo'].trim()
String db       = props['db'].trim()
String dbid     = props['dbid'].trim()
String recordId = props['recordId'].trim()
String relatedChangeRequestTitle = props['relatedChangeRequestTitle'].trim()
String relatedChangeRequestLink  = props['relatedChangeRequestLink'].trim()
boolean acceptAllCertificates    = props['acceptAllCertificates'].toBoolean()

ClearQuestHelper cqh = new ClearQuestHelper(hostname, repo, db, dbid, recordId,
     username, password, acceptAllCertificates)

try {
    cqh.addRelatedChangeRequest(relatedChangeRequestTitle, relatedChangeRequestLink)
}
catch (Exception ex) {
    println "[Error] Unable to add the Related Change Request Link: '${relatedChangeRequestTitle}'"
    ex.printStackTrace()
    System.exit(1)
}

println "[Ok] Related Change Request successfully added to record: ${recordId}"
