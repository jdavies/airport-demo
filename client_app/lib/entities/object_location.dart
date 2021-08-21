class ObjectLocation {
  late String name;
  late String type;
  late double x;
  late double y;
  late double rotation; // In degrees.

  ObjectLocation(
      {required this.name,
      required this.type,
      required this.x,
      required this.y,
      required this.rotation}) {}

  ObjectLocation.fromJson(Map<String, dynamic> json) {
    name = json['name'] as String;
    type = json['type'] as String;
    x = double.parse(json['x']);
    y = double.parse(json['y']);
    rotation = double.parse(json['rotation']);
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['name'] = name;
    data['type'] = type;
    data['x'] = x;
    data['y'] = y;
    data['rotation'] = rotation;
    return data;
  }
}
