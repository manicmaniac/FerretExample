BUILD_DIR = build
DEVICE = 'iPhone 8'
XCODEBUILD = xcodebuild -project FerretExample.xcodeproj -scheme FerretExample -configuration Debug -destination name=$(DEVICE) BUILD_DIR=$(BUILD_DIR)

.PHONY: all clean distclean check run

all:
	$(XCODEBUILD) build

clean:
	$(XCODEBUILD) clean

distclean: clean
	$(RM) -R $(BUILD_DIR)

check:
	$(XCODEBUILD) test

run:
	xcrun simctl install $(DEVICE) $(BUILD_DIR)/Debug-iphonesimulator/FerretExample.app
	xcrun simctl launch $(DEVICE) com.github.manicmaniac.FerretExample
