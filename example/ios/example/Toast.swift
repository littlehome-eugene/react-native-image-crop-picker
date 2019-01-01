// https://teabreak.e-spres-oh.com/swift-in-react-native-the-ultimate-guide-part-1-modules-9bb8d054db03
import Foundation
@objc(ToastExample)
class ToastExample: NSObject {

    @objc
    func show(
      _ message: String,
      duration: Int
    ) {
        print("count is \(duration)")
    }



}