import 'package:flutter/material.dart';

import 'object_location.dart';

/**
 * This class represents an aircraft that is in our airspace
 */
class Flight extends ObjectLocation {
  // All aircraft look alike, for now
  static Image aircraft = Image.asset('images/airplane.png');

  Flight(
      {required String name,
      required String type,
      required double x,
      required double y,
      required double rotation})
      : super(name: name, type: type, x: x, y: y, rotation: rotation);
}
