# Minestom Placement
> 'Minestom Placement' is a simple re implementation of **Minecraft vanilla** block placement for [Minestom](https://minestom.net/)

[![](https://jitpack.io/v/ALS-Project/Minestom-Placement.svg)](https://jitpack.io/#ALS-Project/Minestom-Placement)

You can use 'Minestom Placement' as a librarie or as an extension

## Usage example
- ##### Extension Usage:
  Just [download](https://github.com/ALS-Project/Minestom-Placement/releases/latest) the last release build and put it to the extension folder

- ##### Libraries Usage:
  Add the jitpack repository

```groovy
repositories {
  maven { url 'https://jitpack.io' }
}
```

And add the dependencies
```groovy
dependencies {
  implementation 'com.github.ALS-Project:Minestom-Placement:1.0'
}
```

To register **ALL** block placement rules just write this
```java
    BlockPlacementManager.register();
```

To register a specific block
```java
    BlockPlacementManager.register(Block.REDSTONE_TORCH);
```

## Development setup
It's a gradle project, just import it into your favorite IDE !

## License
[![CeCILL License](https://img.shields.io/badge/License-CeCILL-green.svg)](http://www.cecill.info/licences/Licence_CeCILL_V2.1-en.txt)

## Contributing

1. Fork it (<https://github.com/ALS-Project/Minestom-Placement>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request
