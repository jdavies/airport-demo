import 'dart:html';

import 'package:client_app/util/APIManager.dart';
import 'package:flutter/material.dart';
import 'package:web_socket_channel/io.dart';
import 'package:web_socket_channel/status.dart' as status;

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Airport Viewer'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, required this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  final TextEditingController _controller = TextEditingController();
  final token =
      'eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjbGllbnQ7ZDkwOWFiMGItODRlNy00MGI0LWFjZGMtNDdjNmY5MmRiMjk5O1lXbHljRzl5ZEMxbGRtVnVkSE09In0.W597zdJaUfwjSiDxpR-Gz3BlPlA_noPhZzKz0iCIM61E5vwCbaDVqTR8F_nw5VMyuMnhssCYmpxL_VqqNqeJzn3Tg9HkS2EGPHe_6XJHAuj-DlP_S_6MnioceCIPEFY2fDQrPkf6JFvf1voX19hDY-mcn7dpBy5PJh9GO9B7dPfFkGL-kmCfCZkvE4h9SSPc-hkj5MD0ezkVXwpVspFDmZhwmF4HimkQZ8zsPp9eQD1NUJTyWcI8Cn5S5aKM6OtoqtCeUOEr4rngG4XLVkHxeZejCUOSocVyAuc0yLDThgfn2-dPr6SwzZf6r1gP24g0-H-v-wM82NnhatGm66kCdw';
  final String authHeader =
      'Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjbGllbnQ7ZDkwOWFiMGItODRlNy00MGI0LWFjZGMtNDdjNmY5MmRiMjk5O1lXbHljRzl5ZEMxbGRtVnVkSE09In0.W597zdJaUfwjSiDxpR-Gz3BlPlA_noPhZzKz0iCIM61E5vwCbaDVqTR8F_nw5VMyuMnhssCYmpxL_VqqNqeJzn3Tg9HkS2EGPHe_6XJHAuj-DlP_S_6MnioceCIPEFY2fDQrPkf6JFvf1voX19hDY-mcn7dpBy5PJh9GO9B7dPfFkGL-kmCfCZkvE4h9SSPc-hkj5MD0ezkVXwpVspFDmZhwmF4HimkQZ8zsPp9eQD1NUJTyWcI8Cn5S5aKM6OtoqtCeUOEr4rngG4XLVkHxeZejCUOSocVyAuc0yLDThgfn2-dPr6SwzZf6r1gP24g0-H-v-wM82NnhatGm66kCdw';

  final String TOPIC =
      'wss://pulsar-aws-useast2.streaming.datastax.com:8001/ws/v2/consumer/persistent/airport-events/airport/object-location/my-subscription';

  _MyHomePageState() {
    Map<String, dynamic> headers = {"Authorization": authHeader};

    var channel =
        IOWebSocketChannel.connect(Uri.parse(TOPIC), headers: headers);

    channel.stream.listen((message) {
      channel.sink.add('received!');
      channel.sink.close(status.goingAway);
    });

    // WebSocket ws = WebSocket(TOPIC, headers);
    // ws.onMessage.listen((event) {
    //   print(event.toString());
    //   });
  }

  void _incrementCounter() {
    setState(() {
      _counter++;
      APIManager.getObjectLocations();
    });
  }

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Image.asset('images/Airport.png'),
            Text(
              'You have pushed the button this many times:',
            ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headline4,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
