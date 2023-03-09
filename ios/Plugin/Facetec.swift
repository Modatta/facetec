import Foundation

@objc public class Facetec: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
