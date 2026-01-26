# Spindler - GEDCOM Kotlin Multiplatform Parser 🌳

**Spindler** is a delightfully powerful Kotlin Multiplatform Compose library that transforms GEDCOM
genealogy files into beautiful, type-safe Kotlin models! 🎉

Whether you're building a family tree app, analysing genealogical data, or just want to explore your
family history programmatically, Spindler got you covered! It handles everything from **marriage
records, family relationships** and most important of all tricky **date formats** from the past
centuries!

Built with modern Kotlin Multiplatform magic ✨, it works seamlessly across Android, iOS, and  
Desktop - because family trees shouldn't be platform-locked!

![screenshot_overview](screenshots/0_overview.gif)

> Try out the `sample` app!

## 🌟 Features

- 📊 **Complete GEDCOM Parsing** — Transform GEDCOM 5.5.1, 5.5.5 and 7.0 files into clear, structured
  Kotlin models
- 👥 **Family Relationship Mapping** — Navigate complex family trees with ease (parents, children,
  spouses)
- 📅 **Smart Date Parsing** — Handles historical dates, partial dates, and multiple formats  
  automatically
- 🌍 **KMP Ready** — Works on Android, iOS, Desktop, and anywhere Kotlin runs
- 🔗 **Flexible Data Sources** — Load from local files, remote URLs, or raw strings
- 🏷️ **MacFamilyTree Extensions** — Full support for MacFamilyTree-specific tags and features
- 📍 **Rich Location Data** — Birth places, death places, marriage locations with full detail
- 🎯 **Type-Safe Models** — No more string parsing headaches - everything is properly typed
- ⚡ **Lightweight & Fast** — Minimal dependencies, maximum performance
- 🧠 **Smart Defaults** — Gracefully handles missing data with sensible fallbacks

## 🎭 What Makes It Special?

1. **Built for Real Genealogy** — Tested with actual family history data, weird edge cases included!
2. **Date Intelligence** — Parses "ABT 1845", "BEF 1900", "EST 1820" and countless historical date  
   formats
3. **Relationship Navigation** — Find someone's parents, children, or spouse with simple property  
   access
4. **MacFamilyTree Ready** — Seamlessly works with popular genealogy software exports
5. **Multiplatform Native** — Same API across all platforms, no compromises

## 📦 Installation

Add the dependency in your `build.gradle.kts`:

### Step 1

Add the mavenCentral repository to your `settings.gradle.kts` file:

```groovy
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
```

### Step 2

Add the dependency:

```groovy
dependencies {
    implementation("io.github.delacrixmorgan:spindler:X.X.X")
}
```

## 🚀 Quick Start

### Loading from Local Date Source

If you're reading from a local file on device.

```kotlin
object SpindlerLocalDataSource {
    private val path: String = "files/sample.ged"
    private val gedcomIndexDtoToModelMapper: GedcomIndexDtoToModelMapper by lazy { GedcomIndexDtoToModelMapper() }

    suspend fun getData(): GedcomIndex {
        val text = Res.readBytes(path = path).decodeToString()
        return gedcomIndexDtoToModelMapper(text)
    }
}

// Usage  
val familyTree = SpindlerLocalDataSource.getData()
println("Found ${familyTree.individuals.size} individuals!")
println("Found ${familyTree.families.size} families!")  
```  

### Loading from Remote Data Source

If you're reading from an API that returns `.ged`.

```kotlin
object SpindlerRemoteDataSource {
    private val httpClient = HttpClient()
    private val gedcomMapper = GedcomIndexDtoToModelMapper()

    suspend fun loadData(url: String, headers: Map<String, String>? = null): GedcomIndex {
        try {
            val gedcomContent = httpClient.get(url) {
                headers {
                    append(
                        HttpHeaders.Accept,
                        "text/plain, text/gedcom, application/octet-stream, */*"
                    )
                    headers?.forEach { (key, value) ->
                        append(key, value)
                    }
                }
            }.body<String>()

            return gedcomIndexDtoToModelMapper(gedcomContent)
        } catch (e: Exception) {
            throw RemoteDataSourceException(
                "Failed to download or parse GEDCOM file from $url",
                e
            )
        } finally {
            close()
        }
    }
}

// Usage  
val familyTree = SpindlerRemoteDataSource.loadData("https://example.com/family.ged")
println("Found ${familyTree.individuals.size} individuals!")
println("Found ${familyTree.families.size} families!")   
```  

## 🧬 Data Models

GEDCOM separates the data into groups of `Individual` and `Family`, Spindler is structured similar
to it as well. Within those data models, it consist of `id: String` and `node: List<GedcomNode>`.

```kotlin
data class Individual(
    val id: String,
    val nodes: List<GedcomNode>,
)

data class Family(
    val id: String,
    val nodes: List<GedcomNode>,
)
```

Spindler takes another step further by providing all the **common attributes** in convenient methods
that handles all the mapping. Here's a code snippet within the `Family` data model.

```kotlin
data class Family(
    val id: String,
    val nodes: List<GedcomNode>,
) {
    val marriageDateRaw: String?
        get() = nodes.firstOrNull { it.tag == Tag.MARRIAGE }?.children
            ?.firstOrNull { it.tag == Tag.DATE }?.value

    val marriageDate: LocalDate?
        get() = DateParsing.tryParseDate(marriageDateRaw)

    val marriageDateFormatted: String
        get() = marriageDate?.toString() ?: "~${marriageDateRaw ?: "N/A"}"

    val marriagePlace: String?
        get() = nodes.firstOrNull { it.tag == Tag.MARRIAGE }?.children
            ?.firstOrNull { it.tag == Tag.PLACE }?.value
}
```

### `Individual`

```kotlin  
val individual = familyTree.individuals["I001"]

// Basic Information  
println("Name: ${individual.formattedName}")
println("Given Names: ${individual.givenNames.joinToString(", ")}")
println("Surnames: ${individual.surnames.joinToString(", ")}")
println("Sex: ${individual.sex.name}")

// Life Events  
println("Born: ${individual.birthDateFormatted}")
println("Birth Place: ${individual.birthPlace ?: "Unknown"}")
println("Died: ${individual.deathDateFormatted}")

// Additional Details  
println("Education: ${individual.education ?: "N/A"}")
println("Religion: ${individual.religion ?: "N/A"}")

// Family Relationships  
individual.familyIDAsChild?.let { familyId ->
    val childFamily = familyTree.families[familyId]
    println("Parents' Family: $familyId")
}

individual.familyIDAsSpouse?.let { familyId ->
    val spouseFamily = familyTree.families[familyId]
    println("Spouse Family: $familyId")
}

// MacFamilyTree Integration  
individual.macFamilyTreeID?.let {
    println("MacFamilyTree ID: $it")
}

// Metadata  
println("Last Changed: ${individual.changeDate ?: "N/A"}")
println("Created: ${individual.creationDate ?: "N/A"}")  
```  

### `Family`

```kotlin  
val family = familyTree.families["F001"]

// Marriage Information  
println("Marriage Date: ${family.marriageDateFormatted}")
println("Marriage Place: ${family.marriagePlace ?: "Unknown"}")

// Family Members  
family.husbandID?.let { husbandId ->
    val husband = familyTree.individuals[husbandId]
    println("Husband: ${husband.formattedName}")
}

family.wifeID?.let { wifeId ->
    val wife = familyTree.individuals[wifeId]
    println("Wife: ${wife.formattedName}")
}

// Children  
if (family.childrenIDs.isNotEmpty()) {
    println("Children:")
    family.childrenIDs.forEach { childId ->
        val child = familyTree.individuals[childId]
        println("  - ${child.formattedName}")
    }
}

// MacFamilyTree Extensions  
family.macFamilyTreeLabel?.let {
    println("MacFamilyTree Label: $it")
}  
```  

## 👪 Relationships

Like how GEDCOM structures their data, each `Individual` and `Family`would have their related IDs
store in their data model.

```kotlin
individual.familyIDAsChild
individual.familyIDAsSpouse

family.husbandID
family.wifeID
family.childrenIDs
```

## 📋 Complete API Reference

### `Individual`

| Property             | Type               | Description                              |
|----------------------|--------------------|------------------------------------------|
| `id`                 | `String`           | Unique individual identifier from GEDCOM |
| `formattedName`      | `String`           | Complete name (given names + surnames)   |
| `givenNames`         | `List<String>`     | All given/first names                    |
| `surnames`           | `List<String>`     | All surname/family names                 |
| `sex`                | `Sex`              | Gender (MALE, FEMALE, UNKNOWN)           |
| `birthDate`          | `LocalDate?`       | Parsed birth date (null if unparseable)  |
| `birthDateRaw`       | `String?`          | Original birth date string from GEDCOM   |
| `birthDateFormatted` | `String`           | User-friendly birth date display         |
| `birthPlace`         | `String?`          | Birth location                           |
| `deathDate`          | `LocalDate?`       | Parsed death date (null if unparseable)  |
| `deathDateRaw`       | `String?`          | Original death date string from GEDCOM   |
| `deathDateFormatted` | `String`           | User-friendly death date display         |
| `education`          | `String?`          | Educational information                  |
| `religion`           | `String?`          | Religious affiliation                    |
| `familyIDAsChild`    | `String?`          | Family ID where this person is a child   |
| `familyIDAsSpouse`   | `String?`          | Family ID where this person is a spouse  |
| `macFamilyTreeID`    | `String?`          | MacFamilyTree-specific identifier (_FID) |
| `changeDate`         | `String?`          | Last modification date                   |
| `creationDate`       | `String?`          | Creation date                            |
| `nodes`              | `List<GedcomNode>` | Raw GEDCOM nodes for advanced access     |

### `Family`

| Property                | Type               | Description                                |
|-------------------------|--------------------|--------------------------------------------|
| `id`                    | `String`           | Unique family identifier from GEDCOM       |
| `marriageDate`          | `LocalDate?`       | Parsed marriage date (null if unparseable) |
| `marriageDateRaw`       | `String?`          | Original marriage date string from GEDCOM  |
| `marriageDateFormatted` | `String`           | User-friendly marriage date display        |
| `marriagePlace`         | `String?`          | Marriage location                          |
| `husbandID`             | `String?`          | Individual ID of the husband               |
| `wifeID`                | `String?`          | Individual ID of the wife                  |
| `childrenIDs`           | `List<String>`     | List of individual IDs for all children    |
| `macFamilyTreeLabel`    | `String?`          | MacFamilyTree-specific label               |
| `changeDate`            | `String?`          | Last modification date                     |
| `creationDate`          | `String?`          | Creation date                              |
| `nodes`                 | `List<GedcomNode>` | Raw GEDCOM nodes for advanced access       |

### `GedcomIndex`

| Property      | Type                      | Description                   |
|---------------|---------------------------|-------------------------------|
| `individuals` | `Map<String, Individual>` | All individuals indexed by ID |
| `families`    | `Map<String, Family>`     | All families indexed by ID    |

## 🎯 Advanced Usage

### Custom Date Parsing

Spindler handles complex historical dates automatically:

```kotlin  
// These all parse correctly:  
// "1845"           -> 1845-01-01  
// "ABT 1845"       -> ~1845 (approximate)  
// "BEF 1900"       -> ~1900 (before)  
// "EST 1820"       -> ~1820 (estimated)  
// "25 DEC 1800"    -> 1800-12-25  

val individual = familyTree.individuals["I001"]
individual.birthDate          // LocalDate? - null if not parseable  
individual.birthDateRaw       // String? - Raw GEDCOM text  
individual.birthDateFormatted // String - always has a value  
```  

### Working with Raw Nodes

For advanced use cases or if the methods aren't covered, you can easily use the `GedcomNode` to
access the raw GEDCOM structure to get what you need:

```kotlin  
val individual = familyTree.individuals["I001"]

// Find all custom tags  
val customTags = individual.nodes.filter {
    it.tag.startsWith("_") // Custom tags often start with _}  

// Access specific node data  
    val occupationNode = individual.nodes.firstOrNull { it.tag == "OCCU" }
    val occupation = occupationNode?.value
```  

## 🏗️ Supported Platforms

- ✅ **Android** - API 21+ (Android 5.0+)
- ✅ **Desktop/JVM** - Java 8+
- ✅ **iOS** - iOS 11.0+, all architectures (x64, arm64, simulator arm64)
- 🔄 **Web** - Coming soon!

## 🤝 Contributing

We'd love your help making Spindler even better! Here's how:

1. **Found a bug?** Open an issue with a sample GEDCOM file
2. **Have a feature idea?** Start a discussion - we're always listening!
3. **Want to contribute code?** Fork, branch, code, test, create a PR!
4. **Genealogy expert?** Help us handle more edge cases and formats

## ❤️ Acknowledgments

- [GEDCOM](https://www.gedcom.org/)
- [MacFamilyTree](https://www.syniumsoftware.com/macfamilytree)