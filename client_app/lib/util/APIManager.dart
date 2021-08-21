import 'dart:convert';
import 'dart:core';
import 'package:client_app/entities/object_location.dart';
import 'package:http/http.dart' as http;
import 'credentials.dart';

/**
 * This is a utility class to access the Astra database
 */
class APIManager {
  static String _baseURL = 'https://' +
      Credentials.ASTRA_DB_ID +
      '-' +
      Credentials.ASTRA_DB_REGION +
      '.apps.astra.datastax.com';

  /// Get a list of cities that our airline flies to/from
  static Future<List<ObjectLocation>> getObjectLocations() async {
    // This loads the default preferences.
    final List<ObjectLocation> objList =
        List<ObjectLocation>.empty(growable: true);

    // Now lets make a call to the Document API and get the uer preferences
    // for this specific user
    String url = _baseURL +
        '/api/rest/v2/keyspaces/' +
        Credentials.ASTRA_DB_KEYSPACE +
        '/object_location/rows';

    final response = await http.get(Uri.parse(url), headers: {
      'accept': 'application/json',
      'X-Cassandra-Token': Credentials.ASTRA_DB_TOKEN
    });

    if (response.statusCode == 200) {
      // A successful response looks like the following:
      // {
      //   "count": 1,
      //   "data": [
      //     {
      //       "name": "ABS2045",
      //       "x": "-20.0",
      //       "y": "10.0",
      //       "type": "Boeing 737"
      //     }
      //   ]
      // }
      final jsonResponse = json.decode(response.body);
      if (jsonResponse != null) {
        // int count = jsonResponse['count'] as int;
        final List<dynamic> returnedArray = jsonResponse['data'] as List;

        for (final objLoc in returnedArray) {
          ObjectLocation theObject = ObjectLocation.fromJson(objLoc);
          objList.add(theObject);
        }
      } else if (response.statusCode == 404) {
        // Document not found. Likely a new user. Create a default preferences
        // settings object, store it in the DB and return it to the caller
        // Do nothing
      } else {
        // Error on the GET request
        print(response.body);
      }
    }
    return Future.value(objList);
  }
}
