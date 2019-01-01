// https://teabreak.e-spres-oh.com/swift-in-react-native-the-ultimate-guide-part-1-modules-9bb8d054db03
import Foundation
import YPImagePicker

@objc(ImagePicker)
class ImagePicker: NSObject {
    private var count = 0


    @objc
    func openPicker(
      _ options: Dictionary<String,Any>,
      resolve: @escaping RCTPromiseResolveBlock,
      rejecter reject: @escaping RCTPromiseRejectBlock
    ) -> Void {

        DispatchQueue.main.async  {
            var config = YPImagePickerConfiguration()
            config.library.maxNumberOfItems = 3
            let picker = YPImagePicker(configuration: config)

            picker.didFinishPicking { [unowned picker] items, cancelled in
                let results: NSMutableArray = []

                for item in items {
                    switch item {
                    case .photo(let photo):

                        var asset = photo.asset
                        var image = ImageResult()
                        image.path = "1"
                        results.add(image)

                    case .video:
                        let b = 1
                    }
                }
                resolve(results)

                picker.dismiss(animated: true, completion: nil)

            }

            if let rootViewController = UIApplication.topViewController() {
                rootViewController.present(picker, animated: true, completion: nil)
            }
        }

    }

}
