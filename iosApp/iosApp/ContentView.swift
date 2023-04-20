import UIKit
import SwiftUI
import shared

struct ComposeView: UIViewControllerRepresentable {
    var contentView: any View
    func makeUIViewController(context: Context) -> UIViewController {
        Main_iosKt.MainViewController()
    }
    init(contentView: any View) {
        self.contentView = contentView
    }
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView(contentView: self)
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}



