(require 'objc 'uikit)

(doto (objc/defclass "ViewController" "UIViewController")
  (objc/defmethod "viewDidLoad" "@:"
    (fn [self]
      (let [view (objc/send self "view")]
        (let [color (-> (objc/class-of "UIColor")
                        (objc/send "systemGrayColor"))]
          (objc/send view "setBackgroundColor:" color))
        (let [image (-> (objc/class-of "UIImage")
                        (objc/send "imageNamed:" (objc/str "ferret.png")))
              imageView (doto (-> (objc/class-of "UIImageView")
                                  (objc/send "alloc")
                                  (objc/send "initWithImage:" image))
                          (objc/send "setAutoresizingMask:" (objc/int (bit-or (bit-shift-left 1 1)
                                                                                              (bit-shift-left 1 4))))
                          (objc/send "setContentMode:" (objc/int 1)))]
          (objc/send view "addSubview:" imageView))))))

(doto (objc/defclass "AppDelegate" "UIResponder")
  (objc/defmethod "application:didFinishLaunchingWithOptions:" "@:@@"
    (fn [self application options]
      (let [window (-> (objc/class-of "UIWindow")
                       (objc/send "alloc")
                       (objc/send "init"))]
        (let [viewController (-> (objc/class-of "ViewController")
                                 (objc/send "alloc")
                                 (objc/send "init"))]
          (objc/send window "setRootViewController:" viewController))
        (objc/send window "makeKeyAndVisible"))
      objc/*yes*)))

(uikit/uiapplication-main *command-line-args* "AppDelegate")
