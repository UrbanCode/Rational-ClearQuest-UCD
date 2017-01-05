/**
 *   © Copyright IBM Corporation 2016.
 *   This is licensed under the following license.
 *   The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 *   U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.urbancode.air.plugin.rcq.ClearQuestHelper
import com.urbancode.air.AirPluginTool

def apTool = new AirPluginTool(this.args[0], this.args[1]);
def props = apTool.getStepProperties();

final String username = props['username'].trim()
final String password = props['password']
final String hostname = props['hostname'].trim()
final String repo     = props['repo'].trim()
final String db       = props['db'].trim()
final String dbid     = props['dbid'].trim()
final String recordId = props['recordId'].trim()
final String relatedChangeRequestTitle = props['relatedChangeRequestTitle'].trim()
final String relatedChangeRequestLink  = props['relatedChangeRequestLink'].trim()
final boolean acceptAllCertificates    = props['acceptAllCertificates'].toBoolean()

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
